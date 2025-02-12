public class TestJVisualVM {
    public static void main(String[] args) {
        int a=1;
        while(a == 1){
            JVisualVMTester tester = new JVisualVMTester(a);
            tester.test();
        }
    }
}


