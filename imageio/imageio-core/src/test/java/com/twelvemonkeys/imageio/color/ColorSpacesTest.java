/*
 * Copyright (c) 2011, Harald Kuhr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  Neither the name "TwelveMonkeys" nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.twelvemonkeys.imageio.color;

import org.junit.Test;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;

import static org.junit.Assert.*;

/**
 * ColorSpacesTest
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haraldk$
 * @version $Id: ColorSpacesTest.java,v 1.0 07.02.11 14.32 haraldk Exp$
 */
public class ColorSpacesTest {
    @Test
    public void testCreateColorSpaceFromKnownProfileReturnsInternalCS_sRGB() {
        ICC_Profile profile = ICC_Profile.getInstance(ColorSpace.CS_sRGB);
        ICC_ColorSpace created = ColorSpaces.createColorSpace(profile);
        assertSame(created, ColorSpace.getInstance(ColorSpace.CS_sRGB));
        assertTrue(created.isCS_sRGB());
    }

    @Test
    public void testCreateColorSpaceFromKnownProfileDataReturnsInternalCS_sRGB() {
        ICC_Profile internal = ICC_Profile.getInstance(ColorSpace.CS_sRGB);
        byte[] data = internal.getData();
        assertNotSame(internal.getData(), data); // Sanity check

        ICC_Profile profile = ICC_Profile.getInstance(data);
        assertNotSame(internal, profile); // Sanity check

        ICC_ColorSpace created = ColorSpaces.createColorSpace(profile);
        assertSame(ColorSpace.getInstance(ColorSpace.CS_sRGB), created);
        assertTrue(created.isCS_sRGB());
    }

    @Test
    public void testCreateColorSpaceFromBrokenProfileIsFixedCS_sRGB() {
        ICC_Profile internal = ICC_Profile.getInstance(ColorSpace.CS_sRGB);
        ICC_Profile profile = createBrokenProfile(internal);
        assertNotSame(internal, profile); // Sanity check

        assertTrue(ColorSpaces.isOffendingColorProfile(profile));

        ICC_ColorSpace created = ColorSpaces.createColorSpace(profile);
        assertSame(ColorSpace.getInstance(ColorSpace.CS_sRGB), created);
        assertTrue(created.isCS_sRGB());
    }

    private ICC_Profile createBrokenProfile(ICC_Profile internal) {
        byte[] data = internal.getData();
        data[ICC_Profile.icHdrRenderingIntent] = 1; // Intent: 1 == Relative Colormetric
        return ICC_Profile.getInstance(data);
    }

    @Test
    public void testIsOffendingColorProfile() {
        ICC_Profile broken = createBrokenProfile(ICC_Profile.getInstance(ColorSpace.CS_GRAY));
        assertTrue(ColorSpaces.isOffendingColorProfile(broken));
    }

    @Test
    public void testCreateColorSpaceFromKnownProfileReturnsInternalCS_GRAY() {
        ICC_Profile profile = ICC_Profile.getInstance(ColorSpace.CS_GRAY);
        ICC_ColorSpace created = ColorSpaces.createColorSpace(profile);
        assertSame(ColorSpace.getInstance(ColorSpace.CS_GRAY), created);
    }

    @Test
    public void testCreateColorSpaceFromKnownProfileReturnsInternalCS_PYCC() {
        ICC_Profile profile = ICC_Profile.getInstance(ColorSpace.CS_PYCC);
        ICC_ColorSpace created = ColorSpaces.createColorSpace(profile);
        assertSame(ColorSpace.getInstance(ColorSpace.CS_PYCC), created);
    }

    @Test
    public void testCreateColorSpaceFromKnownProfileReturnsInternalCS_CIEXYZ() {
        ICC_Profile profile = ICC_Profile.getInstance(ColorSpace.CS_CIEXYZ);
        ICC_ColorSpace created = ColorSpaces.createColorSpace(profile);
        assertSame(ColorSpace.getInstance(ColorSpace.CS_CIEXYZ), created);
    }

    @Test
    public void testCreateColorSpaceFromKnownProfileReturnsInternalCS_LINEAR_RGB() {
        ICC_Profile profile = ICC_Profile.getInstance(ColorSpace.CS_LINEAR_RGB);
        ICC_ColorSpace created = ColorSpaces.createColorSpace(profile);
        assertSame(ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB), created);
    }

    @Test
    public void testAdobeRGB98NotNull() {
        assertNotNull(ColorSpaces.getColorSpace(ColorSpaces.CS_ADOBE_RGB_98));
    }

    @Test
    public void testAdobeRGB98IsTypeRGB() {
        assertEquals(ColorSpace.TYPE_RGB, ColorSpaces.getColorSpace(ColorSpaces.CS_ADOBE_RGB_98).getType());
    }

    @Test
    public void testAdobeRGB98AlwaysSame() {
        ColorSpace cs = ColorSpaces.getColorSpace(ColorSpaces.CS_ADOBE_RGB_98);
        assertSame(cs, ColorSpaces.getColorSpace(ColorSpaces.CS_ADOBE_RGB_98));

        if (cs instanceof ICC_ColorSpace) {
            ICC_ColorSpace iccCs = (ICC_ColorSpace) cs;
            assertSame(cs, ColorSpaces.createColorSpace(iccCs.getProfile()));
        }
        else {
            System.err.println("Not an ICC_ColorSpace: " + cs);
        }
    }

    @Test
    public void testCMYKNotNull() {
        assertNotNull(ColorSpaces.getColorSpace(ColorSpaces.CS_GENERIC_CMYK));
    }

    @Test
    public void testCMYKIsTypeCMYK() {
        assertEquals(ColorSpace.TYPE_CMYK, ColorSpaces.getColorSpace(ColorSpaces.CS_GENERIC_CMYK).getType());
    }

    @Test
    public void testCMYKAlwaysSame() {
        ColorSpace cs = ColorSpaces.getColorSpace(ColorSpaces.CS_GENERIC_CMYK);
        assertSame(cs, ColorSpaces.getColorSpace(ColorSpaces.CS_GENERIC_CMYK));

        if (cs instanceof ICC_ColorSpace) {
            ICC_ColorSpace iccCs = (ICC_ColorSpace) cs;
            assertSame(cs, ColorSpaces.createColorSpace(iccCs.getProfile()));
        }
        else {
            System.err.println("Not an ICC_ColorSpace: " + cs);
        }
    }
}