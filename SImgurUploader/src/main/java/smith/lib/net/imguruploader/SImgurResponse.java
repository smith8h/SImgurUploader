package smith.lib.net.imguruploader;

public interface SImgurResponse {
    public void onSuccess(String imageURL);
    public void onError(String message);
}