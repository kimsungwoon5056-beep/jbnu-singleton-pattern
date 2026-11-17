/**
 * AppSettings가 하나의 인스턴스만 만들고, 두 변수가 그 인스턴스를 공유하는지 확인한다.
 */
public class Main {

    public static void main(String[] args) {
        // 1. 인스턴스를 두 번 요청해 서로 다른 변수에 저장
        AppSettings first = AppSettings.getInstance();
        AppSettings second = AppSettings.getInstance();

        System.out.println("[1] Same instance check");
        System.out.println("first == second           : " + (first == second));
        System.out.println("identityHashCode(first)   : " + System.identityHashCode(first));
        System.out.println("identityHashCode(second)  : " + System.identityHashCode(second));
        System.out.println("constructor call count    : " + AppSettings.getCreatedCount());
        System.out.println();

        // 2. 첫 번째 변수로 상태를 바꾸고 두 번째 변수로 읽기
        System.out.println("[2] Shared state check");
        System.out.println("before: second.visitCount = " + second.getVisitCount()
                + ", second.theme = " + second.getTheme());

        first.increaseVisitCount();
        first.increaseVisitCount();
        first.increaseVisitCount();
        first.setTheme("dark");
        System.out.println("changed through first: visitCount +3, theme = dark");

        System.out.println("after : second.visitCount = " + second.getVisitCount()
                + ", second.theme = " + second.getTheme());
        System.out.println();

        // 3. 다시 요청해도 새 객체가 아니라 같은 객체(같은 상태)가 돌아오는지 확인
        AppSettings third = AppSettings.getInstance();
        System.out.println("[3] Request again");
        System.out.println("third == first            : " + (third == first));
        System.out.println("third.visitCount          : " + third.getVisitCount());
        System.out.println("constructor call count    : " + AppSettings.getCreatedCount());
    }
}
