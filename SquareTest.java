import java.lang.reflect.*;
/**
 * This class tests the Square class
 * 
 * @author Mr. Aronson
 */
public class SquareTest
{
    private String className = "Square";
    private  boolean failed = false;
    private  Object t1, t2;
    private  Class<?> c;
    private Constructor constructor;
    Object[] cArgs = {14.0};

    public static void main(String args[]) {
        SquareTest s = new SquareTest();
        s.test();
    }

    public  void test()
    {
        //********** Square Class Test **************************************
        System.out.println("Now testing your Square class \n");
        try
        {
            c = Class.forName(className);
        }
        catch (NoClassDefFoundError e)
        {
            failure("Epic Failure: missing Square class");
        }
        catch (ClassNotFoundException e)
        {
            failure("Epic Failure: missing Square class");
        }
        catch (Exception e) {
            failure(e.toString());
        }

        boolean sideSet = false;
        boolean maxLengthSet = false;
        if(!failed)
        {
            System.out.println("Now testing instance variables and constructor");

            Field[] fields = c.getDeclaredFields();
            if(fields.length == 0)
                failure("Square has no instance variables");
            else
            {
                for(Field field : fields)
                {
                    String temp = field.toString();
                    if (temp.contains("side") && !temp.contains("double"))
                        failure("side instance variable is not a double");
                    else if (temp.contains("side") && !temp.contains("private"))
                        failure("side instance variable is not private");
                    else if (temp.contains("private") && temp.contains("side") && temp.contains("double"))
                    {
                        try
                        {
                            constructor = c.getConstructor(new Class[] {double.class});

                            t1 = constructor.newInstance(cArgs);

                            field.setAccessible(true);
                            double val = (double)field.getDouble(t1);
                            if (Math.abs(val - (double)cArgs[0]) < .001)
                                sideSet = true;
                            else
                                failure("constructor setting side to " + val + " but should be " + cArgs[0]);
                        }
                        catch (NoSuchMethodException e)
                        {
                            failure("missing constructor Square(double theSide)");
                        }
                        catch (Exception e)
                        {
                            failure(e.toString());
                        }
                    }
                    else if (temp.contains("MAX_SIDE_LENGTH") && !temp.contains("final"))
                        failure("MAX_SIDE_LENGTH should be final since it is a constant");
                    else if (temp.contains("MAX_SIDE_LENGTH") && !temp.contains("public"))
                        failure("MAX_SIDE_LENGTH should be public.  Most constants are set to public since they cannot change.");
                    else if (temp.contains("final") && temp.contains("int") && temp.contains("MAX_SIDE_LENGTH"))
                    {
                        try
                        {
                            field.setAccessible(true);
                            Object[] tempArgs = {1};
                            int val = field.getInt(constructor.newInstance(tempArgs));
                            if ( val == 10)
                                maxLengthSet = true;
                            else
                                failure("MAX_SIDE_LENGTH is " + val + "and should be 10");
                        }
                        catch (Exception e)
                        {
                            failure(e.toString());
                        }

                    }
                }
            }
        }

        if(!failed && !sideSet)
            failure("Square instance variable not set to constructor's side parameter");

        if(!failed && !maxLengthSet)
            failure("Square final instance variable MAX_SIDE_LENGTH not set to 10");

        if(!failed)
            System.out.println("Passed instance variables and constructor test\n");

        if(!failed)
        {
            System.out.println("Now testing getSide method");
            try
            {
                Method m = c.getDeclaredMethod("getSide");
                double val = (double)m.invoke(t1);
                if(Math.abs(val - (double)cArgs[0]) > .0001)
                    failure("getSide is " + val + " and should be " + cArgs[0]);
            }
            catch (NoSuchMethodException e)
            {
                failure("missing getSide() method");
            }
            catch (Exception e)
            {
                failure(e.toString());
            }

        }
        if(!failed)
            System.out.println("Passed getSide method test\n");

        // Test setTime method
        if(!failed)
        {
            System.out.println("Now testing setSide method");
            Object args[] = {8.0};

            try
            {
                t2 = constructor.newInstance(args);
                Method m = c.getDeclaredMethod("setSide", double.class);
                Object tempArgs[] = {5.0};
                m.invoke(t2, tempArgs);

            }
            catch (NoSuchMethodException e)
            {
                failure("missing SetSide(double theSide) method");
            }
            catch (Exception e)
            {
                failure(e.toString());
            }

        }

        if(!failed)
        {
            try {
                Method m = c.getDeclaredMethod("getSide");
                double val = (double)m.invoke(t2);

                if (Math.abs(val- 5.0) > .001)
                    failure("setSide is " + val + " and should be 5.0");
            } catch (Exception e) {
                failure(e.toString());
            }
        }

        // Test error checks for setSide method
        if(!failed)
        {
            System.out.println("Now testing if setSide error checks");
            try
            {
                Method m1 = c.getDeclaredMethod("setSide", double.class);
                Object tempArgs[] = {1000};
                m1.invoke(t2, tempArgs);
                Method m2 = c.getDeclaredMethod("getSide");
                double val = (double)m2.invoke(t2);

                if (Math.abs(val - 5.0) > .001)
                    failure("setSide does not error check for the MAX_SIDE_LENGTH");
            }
            catch (NoSuchMethodException e)
            {
                failure("missing setSide() method");
            }
            catch (Exception e) {
                failure(e.toString());
            }

        }
        if(!failed)
        {
            try
            {
                Method m1 = c.getDeclaredMethod("setSide", double.class);
                Object tempArgs[] = {-3};
                m1.invoke(t2, tempArgs);
                Method m2 = c.getDeclaredMethod("getSide");
                double val = (double)m2.invoke(t2);

                if (val < 0)
                    failure("setSide does not error check for negative sides");
            }
            catch (NoSuchMethodException e)
            {
                failure("missing setSide() method");
            }             
            catch (Exception e) {
                failure(e.toString());
            }

        }
        if(!failed)
            System.out.println("Passed setSide method test\n");

        // Test for proper toString() method format
        if(!failed)
        {
            System.out.println("Now testing toString method");
            String objectToString = t1.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(t1));
            if(t1.toString().equals(objectToString))
                failure("missing toString method");
            if(!t1.toString().contains("Square with side length = " + cArgs[0]))
                failure("" + t1.toString() + " is an invalid toString format.  Should be exactly this: \"Square with side length = 14.0\"");

        }

        if(!failed)
            System.out.println("Passed toString method test\n");

        // Test area method
        if(!failed)
        {
            System.out.println("Now testing area method");
            try
            {
                Method m = c.getDeclaredMethod("area");
                double val = (double)m.invoke(t1);

                if(Math.abs(val - 196.0) > .001)
                    failure("The area is " + val + " and should be 196.0");
            }
            catch (NoSuchMethodException e)
            {
                failure("missing area() method");
            }             
            catch (Exception e) {
                failure(e.toString());
            }

        }

        if(!failed)
            System.out.println("Passed area method test\n");

        // Test diagonal method
        if(!failed)
        {
            System.out.println("Now testing diagonal method");
            try
            {
                Object[] tempArgs = {1.0};
                t2 = constructor.newInstance(tempArgs);
                Method m = c.getDeclaredMethod("diagonal");
                double val = (double)m.invoke(t2);

                if(Math.abs(val - 1.414) > .01)
                    failure("The diagonal  is " + val + " and should be 1.414");
            }
            catch (NoSuchMethodException e)
            {
                failure("missing diagonal() method");
            }
            catch (Exception e) {
                failure(e.toString());
            }

        }
        if(!failed)
            System.out.println("Passed diagonal method test\n");

        if(!failed)
        {
            System.out.println("Congratulations, your Square class works correctly \n");
            System.out.println("****************************************************\n");
        }

        if(!failed)
            System.out.println("Yay! You have successfully completed the Square Project!");
        else
            System.out.println("\nBummer.  Try again.");
    }

    private  void failure(String str)
    {
        System.out.println("*** Failed: " + str);
        failed = true;
    }

}
