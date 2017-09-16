package buyme.hackzurich.buyme.domain;

import java.io.File;
import java.util.Map;

/**
 * Created by cecibloom on 16/09/2017.
 */

public interface MultiPartRequest {

    public void addFileUpload(String param,File file);

    public void addStringUpload(String param,String content);

    public Map<String,File> getFileUploads();

    public Map<String,String> getStringUploads();
}
