package mis.kuas.data;

/**
 * Created by mingjia on 2016/2/19.
 */
public interface DataLoggedEvent {

    public static int SUCCESSED = 0;

    public static int FAILED = 1;

    public static int DISCONNECTED = 2;

    public void onDataLogged(int status, String filePath);

}
