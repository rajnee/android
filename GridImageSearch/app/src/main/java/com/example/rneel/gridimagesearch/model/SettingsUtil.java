package com.example.rneel.gridimagesearch.model;

import android.content.res.Resources;

import com.example.rneel.gridimagesearch.R;

/**
 * Created by rneel on 2/1/15.
 */
public class SettingsUtil {
    
    private static Resources resources;
    
    public static void registerResources(Resources resource)
    {
        resources = resource;
    }

    public static int getPositionForImageSize(String imageSize)
    {
        return getPositionInArray(R.array.image_size_array,imageSize);
    }

    public static int getPositionForImageType(String imageType)
    {
        return getPositionInArray(R.array.image_type_array,imageType);
    }

    public static int getPositionForColorFilter(String color)
    {
        return getPositionInArray(R.array.image_color_array,color);
    }

    /* index 0 contains any, so if nothing matches , this is it */
    private static int getPositionInArray(int arrayId, String selectedName)
    {
        if (selectedName == null || selectedName.trim().equals(""))
        {
            return 0;
        }
        String[] res = resources.getStringArray(arrayId);
        
        for (int i = 0; i < res.length; i++)
        {
            if (res[i].equals(selectedName))
            {
                return i;
            }
        }
        
        return  0;
    }
    
    
    
}
