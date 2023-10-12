package me.afoolslove.metalmaxre.patch;

import java.util.List;

public class PatchSegmentWrapperImpl implements IPatchSegmentWrapper {
    private final String description;
    private final List<IPatchSegment> patchSegments;

    public PatchSegmentWrapperImpl(String description, List<IPatchSegment> patchSegments) {
        this.description = description;
        this.patchSegments = patchSegments;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<IPatchSegment> getPatchSegments() {
        return patchSegments;
    }
}
