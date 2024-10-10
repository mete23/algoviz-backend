package de.algoviz.algoviz.generators.export_generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TikZFile with multiple {@link ExportFrameInterface frames}
 *
 * @author Benedikt, Metehan
 * @version 1.0
 */
public class ExportFile {
    private final List<ExportFrameInterface> frameList = new ArrayList<>();

    /**
     * adds a new export-frame into the list
     *
     * @param frame exportFrame instance to add
     */
    public void addFrame(ExportFrameInterface frame) {
        this.frameList.add(frame);
    }

    /**
     * getter-method for frameList
     *
     * @return unmodifiable list with all stored export-frames
     */
    public List<ExportFrameInterface> getFrameList() {
        return Collections.unmodifiableList(frameList);
    }
}
