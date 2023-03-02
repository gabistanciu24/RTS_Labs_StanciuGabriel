public class complexNumbers {
    public static void main(String[] args){
        double realNum1 = 2;
        double imgNum1 = 5;
        double realNum2 = 4;
        double imgNum2 = -1;


        double realSum = realNum1 + realNum2;
        double imgSum = imgNum1 + imgNum2;

        double realProduct = realNum1 * realNum2 - imgNum1 * imgNum2;
        double imgProduct = realNum1 * imgNum2 + imgNum1 * realNum2;

        System.out.println("Sum: " + realSum + " + " + imgSum + "i");
        System.out.println("Product: " + realProduct + " + " + imgProduct + "i");
    }
}
