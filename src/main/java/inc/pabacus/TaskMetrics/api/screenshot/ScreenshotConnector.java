package inc.pabacus.TaskMetrics.api.screenshot;

import inc.pabacus.TaskMetrics.utils.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.utils.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.SslUtil;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScreenshotConnector {

  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

  public boolean uploadFile(File f) {
    List<File> files = new ArrayList<>();
    files.add(f);
    return uploadFiles(files);
  }

  public boolean uploadFiles(List<File> files) {
    try {

      MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
      bodyBuilder.setType(MultipartBody.FORM);

      for (File file : files) {
        bodyBuilder.addFormDataPart("files", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
      }

      RequestBody body = bodyBuilder.build();
      Request request = new Request.Builder()
          .url(new HostConfig().getHost() + "/api/screenshots/uploadfiles")
          .header("Authorization", new StringCacheService().get(CacheKey.TRIBELY_TOKEN))
          .post(body)
          .build();
      Response response = client.newCall(request).execute();
      return response.code() >= 200 && response.code() < 300;
    } catch (IOException e) {
      System.out.println(e);
      return false;
    }
  }


}
