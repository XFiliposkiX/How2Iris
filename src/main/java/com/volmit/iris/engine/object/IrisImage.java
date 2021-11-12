/*
 * Iris is a World Generator for Minecraft Bukkit Servers
 * Copyright (c) 2021 Arcane Arts (Volmit Software)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.plugin.VolmitSender;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class IrisImage extends IrisRegistrant {
    private BufferedImage image;

    public int getRawValue(int x, int z)
    {
        if(x >= w || z >= h || x < 0 || z < 0)
        {
            return 0;
        }

        return image.getRGB(x, z);
    }

    public double getValue(IrisImageChannel channel, int x, int z)
    {
        int color = getRawValue(x + (image.getWidth()/2), z + (image.getHeight()/2));

        switch(channel)
        {
            case RED -> {
                return ((color >> 16) & 0xFF) / 255D;
            }
            case GREEN -> {
                return ((color >> 8) & 0xFF) / 255D;
            }
            case BLUE -> {
                return ((color >> 0) & 0xFF) / 255D;
            }
            case SATURATION -> {
                return Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, (color >> 0) & 0xFF, null)[1];
            }
            case HUE -> {
                return Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, (color >> 0) & 0xFF, null)[0];
            }
            case BRIGHTNESS -> {
                return Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, (color >> 0) & 0xFF, null)[2];
            }
            case COMPOSITE_ADD_RGB -> {
                return ((((color >> 16) & 0xFF) / 255D) + (((color >> 8) & 0xFF) / 255D) + (((color >> 0) & 0xFF) / 255D)) / 3D;
            }
            case COMPOSITE_MUL_RGB -> {
                return (((color >> 16) & 0xFF) / 255D) * (((color >> 8) & 0xFF) / 255D) * (((color >> 0) & 0xFF) / 255D);
            }
            case COMPOSITE_MAX_RGB -> {
                return Math.max(Math.max((((color >> 16) & 0xFF) / 255D), (((color >> 8) & 0xFF) / 255D)), (((color >> 0) & 0xFF) / 255D));
            }
            case COMPOSITE_ADD_HSB -> {
                float[] hsb = Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, (color >> 0) & 0xFF, null);
                return (hsb[0] + hsb[1] + hsb[2]) / 3D;
            }
            case COMPOSITE_MUL_HSB -> {
                float[] hsb = Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, (color >> 0) & 0xFF, null);
                return hsb[0] * hsb[1] * hsb[2];
            }
            case COMPOSITE_MAX_HSB -> {
                float[] hsb = Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, (color >> 0) & 0xFF, null);
                return Math.max(hsb[0], Math.max(hsb[1], hsb[2]));
            }
            case RAW -> {
                return color;
            }
        }
    }

    public IrisImage()
    {
        this(new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB));
    }

    public IrisImage(BufferedImage image)
    {
        this.image = image;
    }

    @Override
    public String getFolderName() {
        return "images";
    }

    @Override
    public String getTypeName() {
        return "Image";
    }

    @Override
    public void scanForErrors(JSONObject p, VolmitSender sender) {

    }
}
