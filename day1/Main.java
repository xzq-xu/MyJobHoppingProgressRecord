public class Main{
    public static void main(String[] args){
        int a = 10;
        new Main().fun1(a);
        System.out.println(a);
    }

    //每个方法由自己的栈空间
    public void fun1(int a){
        int b = 10;
        Person p = new Person();
        p.id = 1;
        p.name = "asd";
        a = a + b;
        //此时a = 20 
        System.out.println(a);
        //但是不影响方法结束时 外部的a依然是10
    }


}


class Person {
    int id;
    String name;
}