package com.bunon.service;

import com.bunon.model.Design;
import com.bunon.repository.DesignRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * F2 logic: spot look-alike designs.
 *
 * How it works (simple "average hash"): shrink the picture to 16x16 grey pixels and mark each pixel
 * brighter/darker than average. Two pictures are alike when most of those marks match.
 */
@Service
public class SimilarityService {

    // From this score upwards two designs count as look-alikes. Tune it if you get too many/few warnings.
    public static final int HASH_THRESHOLD_PERCENT = 90;
    private static final int SIZE = 16;

    /** What we remember about one picture. */
    public record Fingerprint(String hash) { }

    private final DesignRepository designRepository;

    public SimilarityService(DesignRepository designRepository) {
        this.designRepository = designRepository;
    }

    /** Look at an uploaded picture and make its fingerprint. Throws IOException if it is not a real picture. */
    public Fingerprint analyse(MultipartFile file) throws IOException {
        return new Fingerprint(makeHash(file.getBytes()));
    }

    /** Compare with every registered design and return the look-alikes, most similar first. */
    public List<SimilarDesign> findSimilar(Fingerprint mine) {
        List<SimilarDesign> matches = new ArrayList<>();
        for (Design design : designRepository.findAll()) {
            double percent = hashPercent(mine.hash(), design.getFingerprint());
            if (percent >= HASH_THRESHOLD_PERCENT) {
                matches.add(new SimilarDesign(design, percent, "picture check"));
            }
        }
        matches.sort(Comparator.comparingDouble(SimilarDesign::percent).reversed());
        return matches;
    }

    // ---------- the picture check (average hash) ----------

    private String makeHash(byte[] bytes) throws IOException {
        BufferedImage original = ImageIO.read(new ByteArrayInputStream(bytes));
        if (original == null) {
            throw new IOException("Could not read that picture. Please upload a JPG, PNG or GIF.");
        }
        BufferedImage small = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = small.createGraphics();
        g.drawImage(original.getScaledInstance(SIZE, SIZE, Image.SCALE_AREA_AVERAGING), 0, 0, null);
        g.dispose();

        int[] brightness = new int[SIZE * SIZE];
        long sum = 0;
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                int value = small.getRaster().getSample(x, y, 0);
                brightness[y * SIZE + x] = value;
                sum += value;
            }
        }
        double average = (double) sum / brightness.length;

        StringBuilder hash = new StringBuilder();
        for (int value : brightness) {
            hash.append(value > average ? '1' : '0');
        }
        return hash.toString();
    }

    private double hashPercent(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) {
            return 0;
        }
        int different = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                different++;
            }
        }
        return 100.0 * (a.length() - different) / a.length();
    }
}
