/**
 * 프로그램 전체에서 하나만 존재해야 하는 설정 객체 (Singleton).
 */
public final class AppSettings {

    // 유일한 인스턴스를 클래스(static) 필드에 보관한다.
    private static AppSettings instance;

    // 생성자가 실제로 몇 번 호출됐는지 확인하기 위한 값
    private static int createdCount = 0;

    // 공유되는 상태
    private int visitCount = 0;
    private String theme = "light";

    // private 생성자: 클래스 밖에서 new AppSettings()를 호출할 수 없다.
    private AppSettings() {
        createdCount++;
    }

    // 처음 요청할 때만 객체를 만들고, 이후에는 보관해 둔 객체를 그대로 돌려준다.
    public static AppSettings getInstance() {
        if (instance == null) {
            instance = new AppSettings();
        }
        return instance;
    }

    public static int getCreatedCount() {
        return createdCount;
    }

    public void increaseVisitCount() {
        visitCount++;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
