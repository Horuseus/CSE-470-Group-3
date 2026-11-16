package com.bunon.service;

import com.bunon.model.Design;

/** A look-alike: which registered design, how similar (0-100%), and which method measured it. */
public record SimilarDesign(Design design, double percent, String method) { }
