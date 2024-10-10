package de.algoviz.algoviz.generators.export_generator;

/**
 * stores all available kinds of export-frames
 *
 * @author Benedikt
 * @version 1.0
 */
public enum Generators {
    /**
     * export-frame for a 2d graph in the big statement type
     */
    TIKZ_2D_BIG {
        @Override
        public String getName() {
            return "TikZ 2d Big Statement";
        }

        @Override
        public ExportFrameInterface getInstance() {
            return new TikZFrame2dBigStatement();
        }
    },

    /**
     * export-frame for a 2d graph in the compact statement type
     */
    TIKZ_2D_COMPACT {
        @Override
        public String getName() {
            return "TikZ 2d Compact Statement";
        }

        @Override
        public ExportFrameInterface getInstance() {
            return new TikZFrame2dCompactStatement();
        }
    };

    /**
     * getter-method for the name of the export-frame
     *
     * @return name of the export-frame as a string
     */
    public abstract String getName();

    /**
     * getter-method for the concrete instance
     *
     * @return an instance of the exportFrameGenerator as a ExportFrameInterface
     */
    public abstract ExportFrameInterface getInstance();
}
