//package buyme.hackzurich.buyme.ui;
//
//import android.annotation.SuppressLint;
//import android.hardware.Camera;
//import android.os.AsyncTask;
//import android.support.annotation.NonNull;
//import android.system.ErrnoException;
//
//import org.json.JSONArray;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by tatibloom on 16/09/2017.
// */
//
//@SuppressWarnings("deprecation")
//public enum CameraShutter implements Camera.AutoFocusCallback,
//        Camera.PictureCallback, Camera.ShutterCallback, Camera.ErrorCallback {
//
//    INSTANCE;
//
//    private boolean currentlyShutting = false;
//    private Camera.Size optimalSize = null;
//    private OnPictureTakenListener listener;
//    private PictureSaver pictureSaver;
//
//    private enum SavePictureResult {
//        SUCCESS("SUCCESS"), FAIL("FAIL_PROCESSING"), FAIL_NO_SPACE("NO_SPACE");
//        String value;
//
//        SavePictureResult(String value) {
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//    }
//
//
//    public void init(@NonNull final OnPictureTakenListener listener, Camera camera, PictureSaver saver) {
//        this.listener = listener;
//        pictureSaver = saver;
//        camera.setErrorCallback(this);
//
//        searchOptimalSize(camera);
//    }
//
//    private void searchOptimalSize(Camera camera) {
//
//        final Camera.Parameters parameters = camera.getParameters();
//
//        if (optimalSize == null) {
//            final List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
//
//            Camera.Size maxSize = null;
//            for (Camera.Size size : sizes) {
//                if (maxSize == null) {
//                    maxSize = size;
//                } else if ((size.width < (size.height * 2)) && ((size.width > maxSize.width) || (size.width == maxSize.width && size.height <= maxSize.height))) {
//                    maxSize = size;
//                }
//            }
//            optimalSize = maxSize;
//
//        }
//    }
//
//    /**
//     * Schedule a new Shutter operation (may have no effect if there is one
//     * currently in progress)
//     */
//    public void takePicture(CameraPreview preview) {
//        if (currentlyShutting)
//            return;
//        currentlyShutting = true;
//        try {
//            switch (preview.getCamera().getParameters().getFocusMode()) {
//                case Camera.Parameters.FOCUS_MODE_AUTO:
//                case Camera.Parameters.FOCUS_MODE_MACRO:
//                    preview.getCamera().autoFocus(this);
//                    break;
//                default:
//                    onAutoFocus(true, preview.getCamera());
//                    break;
//            }
//
//        } catch (Exception ex) {
//            currentlyShutting = false;
//            listener.onPictureTakenError(OnPictureTakenListener.Error.AUTOFOCUS_ERROR);
//        }
//    }
//
//    /**
//     * Called AFTER AutoFocus called directly by the os
//     */
//    @Override
//    public void onAutoFocus(boolean success, Camera camera) {
//        if (success) {
//            // Compute picture optimal size if never done
//            final Camera.Parameters parameters = camera.getParameters();
//            searchOptimalSize(camera);
//            parameters.setPictureSize(optimalSize.width, optimalSize.height);
//            parameters.setZoom(0);
//            camera.setParameters(parameters);
//
//            // Take the picture
//            camera.takePicture(this, null, this);
//        } else {
//            currentlyShutting = false;
//            listener.onPictureTakenError(OnPictureTakenListener.Error.AUTOFOCUS_ERROR);
//        }
//    }
//
//    /**
//     * Callback called just after shutter operation.
//     * Called as near as possible to the moment when a photo is captured from the sensor.
//     */
//    @Override
//    public void onShutter() {
//        /*
//         * This is done in order to get the current sensors reading at shutter
//         * time. The readings are reused at picture taken
//         */
//        listener.onShutter();
//    }
//
//    /**
//     * Callback called after jpeg preprocessing of the image
//     */
//    @Override
//    public void onPictureTaken(byte[] data, Camera camera) {
//        if (data != null && data.length != 0) {
//            final AsyncImageExporter exporter = new AsyncImageExporter(this, data);
//            pictureSaver.executeSaver(exporter);
//        } else {
//            listener.onPictureTakenError(OnPictureTakenListener.Error.PROCESSING_IMAGE_ERROR);
//        }
//
//    }
//
//    @Override
//    public void onError(int i, Camera camera) {
//        currentlyShutting = false;
//        listener.onPictureTakenError(OnPictureTakenListener.Error.CAMERA_ERROR);
//    }
//
//    /**
//     * Interface for PicturTaken callbacks
//     *
//     * @author B3rn475
//     */
//    public interface OnPictureTakenListener {
//        void onPictureTaken(String file, CameraShutter shutter);
//
//        void onPictureTakenError(Error error);
//
//        void onShutter();
//
//        enum Error {AUTOFOCUS_ERROR, CAMERA_ERROR, NO_SPACE_ERROR, PROCESSING_IMAGE_ERROR}
//    }
//
//    public interface PictureSaver {
//        void executeSaver(AsyncImageExporter saver);
//    }
//
//    /**
//     * Async image processer
//     *
//     * @author jmossina
//     */
//    public class AsyncImageExporter extends AsyncTask<Boolean, Void, String> {
//
//
//        private byte[] data;
//        private final CameraShutter shutter;
//        private final JSONArray peaksInCapture = new JSONArray();
//        private String picturePath;
//        //private Mat capture;
//        private float scaleFactor;
//        String fileName;
//
//
//        AsyncImageExporter(final CameraShutter shutter, byte[] data) {
//            this.data = data;
//            this.shutter = shutter;
//        }
//
//        @Override
//        protected String doInBackground(Boolean... arg0) {
//            if (arg0[0]) {
//                final String date = "asd";
//                fileName = "aaa" + date;
//                //picturePath = ImagesStorageManager.getOriginalPicturePath(fileName);
//                SavePictureResult writeOriginalResult = writeOriginalImage(picturePath, data);
//
//            }
//            return SavePictureResult.FAIL.getValue();
//        }
//
//        @Override
//        protected void onPostExecute(String imgCompletePath) {
//            currentlyShutting = false;
//            if (imgCompletePath == null || imgCompletePath.equals(SavePictureResult.FAIL.getValue())) {
//                listener.onPictureTakenError(OnPictureTakenListener.Error.PROCESSING_IMAGE_ERROR);
//            } else {
//                if (imgCompletePath.equals(SavePictureResult.FAIL_NO_SPACE.getValue())) {
//                    listener.onPictureTakenError(OnPictureTakenListener.Error.NO_SPACE_ERROR);
//                } else {
//                    listener.onPictureTaken(imgCompletePath, shutter);
//                }
//            }
//        }
//
//        private SavePictureResult writeOriginalImage(String path, byte[] image) {
//            SavePictureResult success = SavePictureResult.SUCCESS;
//
//            try {
//                OutputStream outputStream = new FileOutputStream(path);
//                outputStream.write(image);
//                outputStream.close();
//            } catch (IOException ex) {
//                boolean itsanenospcex = false;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    // make sure the cause is an ErrnoException
//                    if (ex.getCause() != null && ex.getCause() instanceof ErrnoException) {
//                        // if so, we can get to the causing errno
//                        int errno;
//                        errno = ((ErrnoException) ex.getCause()).errno;
//
//                        // and check for the appropriate value
//                        itsanenospcex = errno == android.system.OsConstants.ENOSPC;
//                    }
//                }
//                if (itsanenospcex) {
//                    success = SavePictureResult.FAIL_NO_SPACE;
//                } else {
//                    success = SavePictureResult.FAIL;
//                }
//                ex.printStackTrace();
//            } catch (Exception e) {
//                success = SavePictureResult.FAIL;
//            }
//            return success;
//        }
//
//    }
//
//}