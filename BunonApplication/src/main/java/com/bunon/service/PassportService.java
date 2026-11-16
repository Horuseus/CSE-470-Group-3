package com.bunon.service;

import com.bunon.model.PassportEntry;
import com.bunon.model.Product;
import com.bunon.model.ProductPassport;
import com.bunon.repository.PassportEntryRepository;
import com.bunon.repository.ProductPassportRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.List;

/** F3 logic: create passports, add history entries, check nobody changed history, draw QR codes. */
@Service
public class PassportService {

    private final ProductPassportRepository passportRepository;
    private final PassportEntryRepository entryRepository;

    public PassportService(ProductPassportRepository passportRepository, PassportEntryRepository entryRepository) {
        this.passportRepository = passportRepository;
        this.entryRepository = entryRepository;
    }

    /** Called whenever a new product is created. */
    public ProductPassport createFor(Product product) {
        ProductPassport passport = passportRepository.save(new ProductPassport(product));
        addEntry(passport, "LISTED", "Listed on Bunon by " + product.getWeaver().getName());
        return passport;
    }

    /** Adds a history line and links it to the previous line by hash. */
    public PassportEntry addEntry(ProductPassport passport, String eventType, String details) {
        String prevHash = entryRepository.findTopByPassportIdOrderByIdDesc(passport.getId())
                .map(PassportEntry::getHash)
                .orElse("START");
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        PassportEntry entry = new PassportEntry(passport, eventType, details, now, prevHash);
        entry.setHash(computeHash(entry));
        return entryRepository.save(entry);
    }

    /** Walks through the history. Returns false if any entry was changed after it was written. */
    public boolean verifyChain(Long passportId) {
        List<PassportEntry> entries = entryRepository.findByPassportIdOrderByIdAsc(passportId);
        String expectedPrev = "START";
        for (PassportEntry entry : entries) {
            boolean linkOk = entry.getPrevHash().equals(expectedPrev);
            boolean contentOk = entry.getHash().equals(computeHash(entry));
            if (!linkOk || !contentOk) {
                return false;
            }
            expectedPrev = entry.getHash();
        }
        return true;
    }

    /** SHA-256 "fingerprint" of an entry's content plus the previous hash. */
    private String computeHash(PassportEntry e) {
        String data = e.getPrevHash() + "|" + e.getEventType() + "|" + e.getDetails() + "|" + e.getCreatedAt();
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256").digest(data.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /** Draws a QR code picture (PNG bytes) that contains the given text/URL. */
    public byte[] makeQrPng(String text) throws Exception {
        BitMatrix matrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, 320, 320);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
    }
}
