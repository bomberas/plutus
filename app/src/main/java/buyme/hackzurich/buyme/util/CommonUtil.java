package buyme.hackzurich.buyme.util;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;

/**
 * This is an utility class.
 *
 * <p>All methods of this class are helpers of the project.
 * <p>Each method is responsible to perform some minor action non related to the mechanics of the game
 * but helpful and used in various instances.
 *
 * @author cecibloom
 * @author megireci
 * @author tatibloom
 * @version 1.0
 * @since 16/09/2017.
 */
public class CommonUtil {

    /**
     * Checks whether a collection is empty or not.
     *
     * @param collection collection that wants to be validated.
     * @return  {@code true} if the collection is empty.
     *          {@code true} if not.
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    /**
     * Checks whether a string is empty or not.
     *
     * @param string string that wants to be validated.
     * @return  {@code true} if the given string is empty.
     *          {@code true} if not.
     */
    public static boolean isEmpty(String string) {
        return string == null || string.equals("");
    }

    /**
     * Checks whether two strings are equal without regarding the case.
     *
     * @param s1 string to compare.
     * @param s2 string to compare.
     * @return [null,null] {@code true} <br/>
     *         ["  123  ","123"] {@code true} <br/>
     *         ["ABC","abc"] {@code true} <br/>
     *         {@code false} in any other case.
     */
    public static boolean equalsIgnoreCase(String s1, String s2) {
        if ( s1 == null ) return s2 == null;
        if ( s2 == null ) return false;
        if ( s1.trim().equals("") ) return s2.trim().equals("");
        return !s2.trim().equals("") && s1.toLowerCase().trim().equals(s2.toLowerCase().trim());
    }

    /**
     *
     * Clones the given array and creates another array exactly like the given one, in all forms and shapes.
     *
     * @param source array to be cloned
     * @return {@code array} cloned after method
     */
    public static int[][] cloneArray( int[][] source) {
        int[][] dest = new int[source.length][source.length];
        for ( int col = 0; col < source.length; col++) {
            for ( int row = 0; row < source.length; row++) {
                dest[row][col] = source[row][col];
            }
        }
        return dest;
    }

    /**
     *
     * Creates a toast message on the screen displaying a customized <tt>text</tt>text
     *
     * @param context The context in which the Toast message should be created
     * @param msg The message display in the Toast message
     * @param duration The duration of the message on the screen. It's either
     *              {@link Toast#LENGTH_LONG} or {@link Toast#LENGTH_SHORT}
     */
    public static void showToastMessage(Context context, View view, TextView text, String msg, int duration){

        text.setText(msg);

        Toast myToast = new Toast(context);
        myToast.setView(view);
        myToast.setDuration(duration);
        myToast.setGravity(Gravity.CENTER , 0, 0);
        myToast.show();
    }

    public static void setColor(ImageView v, int val){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(val);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(filter);
    }
}