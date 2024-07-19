package com.gift3dconc3pts.batch.processing.batch.listeners.error;

import com.gift3dconc3pts.batch.processing.core.model.entities.FileStaging;
import com.gift3dconc3pts.batch.processing.core.model.entities.FileUploadJobHeader;
import com.gift3dconc3pts.batch.processing.core.model.entities.ProcessingError;
import com.gift3dconc3pts.batch.processing.core.model.entities.StepTypeError;
import com.gift3dconc3pts.batch.processing.core.services.ProcessingErrorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;

import javax.annotation.Nullable;

@RequiredArgsConstructor
@Slf4j
public class StageFileDataReaderErrorListener implements ItemReadListener<FileStaging> {

    private final Long fileUploadJobHeaderId;
    private final ProcessingErrorService processingErrorService;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void onReadError(@Nullable Exception exception) {
        final ProcessingError processingError = ProcessingError.builder().fileUploadJobHeader(FileUploadJobHeader
                        .builder().jobHeaderId(fileUploadJobHeaderId).build()).stepTypeError(StepTypeError.FILEUPLOADREADER)
                .build();

        if (exception instanceof IncorrectTokenCountException incorrectTokenCountException) {
            String errorMessage = incorrectTokenCountException.getMessage();
            log.error("FlatFormatException: {}", errorMessage);
            processingError.setProcessingErrorDescription(errorMessage);
        } else if (exception instanceof FlatFileParseException flatFileParseException) {
            String errorMessage = String.format("Line: %o Input: %s", flatFileParseException.getLineNumber(),
                    flatFileParseException.getInput());
            log.error(errorMessage);
            processingError.setProcessingErrorDescription(errorMessage);
        } else {
            String errorMessage = exception.getMessage();
            log.error("Encountered error on read: {}", errorMessage);
            processingError.setProcessingErrorDescription(errorMessage);
        }

        processingErrorService.saveProcessingError(processingError);
    }
}
