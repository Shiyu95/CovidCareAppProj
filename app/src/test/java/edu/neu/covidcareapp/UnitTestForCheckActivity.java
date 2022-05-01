//package edu.neu.covidcareapp;
//
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//
//import junit.framework.TestCase;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//
//import kotlin.Suppress;
//
//
///**
// * This test case for testing check activity
// */
//public class UnitTestForCheckActivity extends TestCase {
//    protected Context mContext;
//    private Context mTestContext;
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//    }
//    @Override
//    protected void tearDown() throws Exception {
//        super.tearDown();
//    }
//    //if we didn't got context infor, we just print the mesaage for alert.
//    @Suppress(names = {})
//    public void testAndroidTestCaseSetupProperly() {
//        assertNotNull("Context is null. setContext should be called before tests are run",
//                mContext);
//    }
//    public void setContext(Context context) {
//        mContext = context;
//    }
//    public Context getContext() {
//        return mContext;
//    }
//    /**
//     * Test context can be used to access resources from the test's own package
//     * as opposed to the resources from the test target package.
//     */
//    public void setTestContext(Context context) {
//        mTestContext = context;
//    }
//    public Context getTestContext() {
//        return mTestContext;
//    }
//    /**
//     * Asserts that launching a given activity is protected by a particular permission by
//     * attempting to start the activity and validating that a {@link SecurityException}
//     * is thrown that mentions the permission in its error message.
//     *
//     * Note that an instrumentation isn't needed because all we are looking for is a security error
//     * and we don't need to wait for the activity to launch and get a handle to the activity.
//     *
//     * @param packageName The package name of the activity to launch.
//     * @param className The class of the activity to launch.
//     * @param permission The name of the permission.
//     */
//    public void assertActivityRequiresPermission(
//            String packageName, String className, String permission) {
//        final Intent intent = new Intent();
//        intent.setClassName(packageName, className);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        try {
//            getContext().startActivity(intent);
//            fail("expected security exception for " + permission);
//        } catch (SecurityException expected) {
//            assertNotNull("security exception's error message.", expected.getMessage());
//            assertTrue("error message should contain " + permission + ".",
//                    expected.getMessage().contains(permission));
//        }
//    }
//    /**
//     * Asserts that reading from the content uri requires a particular permission by querying the
//     * uri and ensuring a {@link SecurityException} is thrown mentioning the particular permission.
//     *
//     * @param uri The uri that requires a permission to query.
//     * @param permission The permission that should be required.
//     */
//    public void assertReadingContentUriRequiresPermission(Uri uri, String permission) {
//        try {
//            getContext().getContentResolver().query(uri, null, null, null, null);
//            fail("expected SecurityException requiring " + permission);
//        } catch (SecurityException expected) {
//            assertNotNull("security exception's error message.", expected.getMessage());
//            assertTrue("error message should contain " + permission + ".",
//                    expected.getMessage().contains(permission));
//        }
//    }
//    /**
//     * Asserts that writing to the content uri requires a particular permission by inserting into
//     * the uri and ensuring a {@link SecurityException} is thrown mentioning the particular
//     * permission.
//     *
//     * @param uri The uri that requires a permission to query.
//     * @param permission The permission that should be required.
//     */
//    public void assertWritingContentUriRequiresPermission(Uri uri, String permission) {
//        try {
//            getContext().getContentResolver().insert(uri, new ContentValues());
//            fail("expected SecurityException requiring " + permission);
//        } catch (SecurityException expected) {
//            assertNotNull("security exception's error message.", expected.getMessage());
//            assertTrue("error message should contain \"" + permission + "\". Got: \""
//                            + expected.getMessage() + "\".",
//                    expected.getMessage().contains(permission));
//        }
//    }
//    /**
//     * This function is called by various TestCase implementations, at tearDown() time, in order
//     * to scrub out any class variables.  This protects against memory leaks in the case where a
//     * test case creates a non-static inner class (thus referencing the test case) and gives it to
//     * someone else to hold onto.
//     *
//     * @param testCaseClass The class of the derived TestCase implementation.
//     *
//     * @throws IllegalAccessException
//     */
//    protected void scrubClass(final Class<?> testCaseClass)
//            throws IllegalAccessException {
//        final Field[] fields = getClass().getDeclaredFields();
//        for (Field field : fields) {
//            if (!field.getType().isPrimitive() &&
//                    !Modifier.isStatic(field.getModifiers())) {
//                try {
//                    field.setAccessible(true);
//                    field.set(this, null);
//                } catch (Exception e) {
//                    android.util.Log.d("TestCase", "Error: Could not nullify field!");
//                }
//                if (field.get(this) != null) {
//                    android.util.Log.d("TestCase", "Error: Could not nullify field!");
//                }
//            }
//        }
//    }
//}