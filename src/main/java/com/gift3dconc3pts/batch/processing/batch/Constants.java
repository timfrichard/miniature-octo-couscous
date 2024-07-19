package com.gift3dconc3pts.batch.processing.batch;

public class Constants {

    /**
     * Name of the job
     */
    public static final String JOB_NAME = "jobFileUploadProcessing";

    public static final String PARAMETERS_JOB_HEADER_ID = "jobHeaderId";

    public static final String PARAMETERS_JOB_START_VALUE = "jobStartValue";

    public static final String PARAMETERS_JOB_SAVED_FILE_NAME = "jobFileName";

    public static final String PARAMETERS_TAS_BETC_FILE_NAME = "tastbetFile";

    public static final String STEP_FILE_UPLOAD = "stepFileUpload";

    public static final String STEP_CLAM_AV = "stepClamAV";

    public static final String STEP_STAGE_FILE_DATA = "stepStageFileData";

    public static final String FILE_UPLOAD_READER_ITEM = "fileUploadItemReader";

    public static final String STEP_TAS_BETC_API_CALL = "stepTasBetcAPICall";

    private Constants() {
        // no op
    }
}
