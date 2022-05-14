package net.runelite.client.external.adonaicore.utils;

import com.google.common.annotations.VisibleForTesting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colors
{

	public static final int[] Aliceblue = new int[]{0xf0, 0xf8, 0xff}; // rgb(240, 248, 255.0f)
	public static final int[] Antiquewhite = new int[]{0xfa, 0xeb, 0xd7}; // rgb(250, 235, 215)
	public static final int[] Aqua = new int[]{0x00, 0xff, 0xff}; // rgb(0, 255.0f, 255.0f)
	public static final int[] Aquamarine = new int[]{0x7f, 0xff, 0xd4}; // rgb(127, 255.0f, 212)
	public static final int[] Azure = new int[]{0xf0, 0xff, 0xff}; // rgb(240, 255.0f, 255.0f)
	public static final int[] Beige = new int[]{0xf5, 0xf5, 0xdc}; // rgb(245, 245, 220)
	public static final int[] Bisque = new int[]{0xff, 0xe4, 0xc4}; // rgb(255.0f, 228, 196)
	public static final int[] Black = new int[]{0x00, 0x00, 0x00}; // rgb(0, 0, 0)
	public static final int[] Blanchedalmond = new int[]{0xff, 0xeb, 0xcd}; // rgb(255.0f, 235, 205)
	public static final int[] Blue = new int[]{0x00, 0x00, 0xff}; // rgb(0, 0, 255.0f)
	public static final int[] Blueviolet = new int[]{0x8a, 0x2b, 0xe2}; // rgb(138, 43, 226)
	public static final int[] Brown = new int[]{0xa5, 0x2a, 0x2a}; // rgb(165, 42, 42)
	public static final int[] Burlywood = new int[]{0xde, 0xb8, 0x87}; // rgb(222, 184, 135)
	public static final int[] Cadetblue = new int[]{0x5f, 0x9e, 0xa0}; // rgb(95, 158, 160)
	public static final int[] Chartreuse = new int[]{0x7f, 0xff, 0x00}; // rgb(127, 255.0f, 0)
	public static final int[] Chocolate = new int[]{0xd2, 0x69, 0x1e}; // rgb(210, 105, 30)
	public static final int[] Coral = new int[]{0xff, 0x7f, 0x50}; // rgb(255.0f, 127, 80)
	public static final int[] Cornflowerblue = new int[]{0x64, 0x95, 0xed}; // rgb(100, 149, 237)
	public static final int[] Cornsilk = new int[]{0xff, 0xf8, 0xdc}; // rgb(255.0f, 248, 220)
	public static final int[] Crimson = new int[]{0xdc, 0x14, 0x3c}; // rgb(220, 20, 60)
	public static final int[] Cyan = new int[]{0x00, 0xff, 0xff}; // rgb(0, 255.0f, 255.0f)
	public static final int[] Darkblue = new int[]{0x00, 0x00, 0x8b}; // rgb(0, 0, 139)
	public static final int[] Darkcyan = new int[]{0x00, 0x8b, 0x8b}; // rgb(0, 139, 139)
	public static final int[] Darkgoldenrod = new int[]{0xb8, 0x86, 0x0b}; // rgb(184, 134, 11)
	public static final int[] Darkgray = new int[]{0xa9, 0xa9, 0xa9}; // rgb(169, 169, 169)
	public static final int[] Darkgreen = new int[]{0x00, 0x64, 0x00}; // rgb(0, 100, 0)
	public static final int[] Darkgrey = new int[]{0xa9, 0xa9, 0xa9}; // rgb(169, 169, 169)
	public static final int[] Darkkhaki = new int[]{0xbd, 0xb7, 0x6b}; // rgb(189, 183, 107)
	public static final int[] Darkmagenta = new int[]{0x8b, 0x00, 0x8b}; // rgb(139, 0, 139)
	public static final int[] Darkolivegreen = new int[]{0x55, 0x6b, 0x2f}; // rgb(85, 107, 47)
	public static final int[] Darkorange = new int[]{0xff, 0x8c, 0x00}; // rgb(255.0f, 140, 0)
	public static final int[] Darkorchid = new int[]{0x99, 0x32, 0xcc}; // rgb(153, 50, 204)
	public static final int[] Darkred = new int[]{0x8b, 0x00, 0x00}; // rgb(139, 0, 0)
	public static final int[] Darksalmon = new int[]{0xe9, 0x96, 0x7a}; // rgb(233, 150, 122)
	public static final int[] Darkseagreen = new int[]{0x8f, 0xbc, 0x8f}; // rgb(143, 188, 143)
	public static final int[] Darkslateblue = new int[]{0x48, 0x3d, 0x8b}; // rgb(72, 61, 139)
	public static final int[] Darkslategray = new int[]{0x2f, 0x4f, 0x4f}; // rgb(47, 79, 79)
	public static final int[] Darkslategrey = new int[]{0x2f, 0x4f, 0x4f}; // rgb(47, 79, 79)
	public static final int[] Darkturquoise = new int[]{0x00, 0xce, 0xd1}; // rgb(0, 206, 209)
	public static final int[] Darkviolet = new int[]{0x94, 0x00, 0xd3}; // rgb(148, 0, 211)
	public static final int[] Deeppink = new int[]{0xff, 0x14, 0x93}; // rgb(255.0f, 20, 147)
	public static final int[] Deepskyblue = new int[]{0x00, 0xbf, 0xff}; // rgb(0, 191, 255.0f)
	public static final int[] Dimgray = new int[]{0x69, 0x69, 0x69}; // rgb(105, 105, 105)
	public static final int[] Dimgrey = new int[]{0x69, 0x69, 0x69}; // rgb(105, 105, 105)
	public static final int[] Dodgerblue = new int[]{0x1e, 0x90, 0xff}; // rgb(30, 144, 255.0f)
	public static final int[] Firebrick = new int[]{0xb2, 0x22, 0x22}; // rgb(178, 34, 34)
	public static final int[] Floralwhite = new int[]{0xff, 0xfa, 0xf0}; // rgb(255.0f, 250, 240)
	public static final int[] Forestgreen = new int[]{0x22, 0x8b, 0x22}; // rgb(34, 139, 34)
	public static final int[] Fuchsia = new int[]{0xff, 0x00, 0xff}; // rgb(255.0f, 0, 255.0f)
	public static final int[] Gainsboro = new int[]{0xdc, 0xdc, 0xdc}; // rgb(220, 220, 220)
	public static final int[] Ghostwhite = new int[]{0xf8, 0xf8, 0xff}; // rgb(248, 248, 255.0f)
	public static final int[] Gold = new int[]{0xff, 0xd7, 0x00}; // rgb(255.0f, 215, 0)
	public static final int[] Goldenrod = new int[]{0xda, 0xa5, 0x20}; // rgb(218, 165, 32)
	public static final int[] Gray = new int[]{0x80, 0x80, 0x80}; // rgb(128, 128, 128)
	public static final int[] Green = new int[]{0x00, 0x80, 0x00}; // rgb(0, 128, 0)
	public static final int[] Greenyellow = new int[]{0xad, 0xff, 0x2f}; // rgb(173, 255.0f, 47)
	public static final int[] Grey = new int[]{0x80, 0x80, 0x80}; // rgb(128, 128, 128)
	public static final int[] Honeydew = new int[]{0xf0, 0xff, 0xf0}; // rgb(240, 255.0f, 240)
	public static final int[] Hotpink = new int[]{0xff, 0x69, 0xb4}; // rgb(255.0f, 105, 180)
	public static final int[] Indianred = new int[]{0xcd, 0x5c, 0x5c}; // rgb(205, 92, 92)
	public static final int[] Indigo = new int[]{0x4b, 0x00, 0x82}; // rgb(75, 0, 130)
	public static final int[] Ivory = new int[]{0xff, 0xff, 0xf0}; // rgb(255.0f, 255.0f, 240)
	public static final int[] Khaki = new int[]{0xf0, 0xe6, 0x8c}; // rgb(240, 230, 140)
	public static final int[] Lavender = new int[]{0xe6, 0xe6, 0xfa}; // rgb(230, 230, 250)
	public static final int[] Lavenderblush = new int[]{0xff, 0xf0, 0xf5}; // rgb(255.0f, 240, 245)
	public static final int[] Lawngreen = new int[]{0x7c, 0xfc, 0x00}; // rgb(124, 252, 0)
	public static final int[] Lemonchiffon = new int[]{0xff, 0xfa, 0xcd}; // rgb(255.0f, 250, 205)
	public static final int[] Lightblue = new int[]{0xad, 0xd8, 0xe6}; // rgb(173, 216, 230)
	public static final int[] Lightcoral = new int[]{0xf0, 0x80, 0x80}; // rgb(240, 128, 128)
	public static final int[] Lightcyan = new int[]{0xe0, 0xff, 0xff}; // rgb(224, 255.0f, 255.0f)
	public static final int[] Lightgoldenrodyellow = new int[]{0xfa, 0xfa, 0xd2}; // rgb(250, 250, 210)
	public static final int[] Lightgray = new int[]{0xd3, 0xd3, 0xd3}; // rgb(211, 211, 211)
	public static final int[] Lightgreen = new int[]{0x90, 0xee, 0x90}; // rgb(144, 238, 144)
	public static final int[] Lightgrey = new int[]{0xd3, 0xd3, 0xd3}; // rgb(211, 211, 211)
	public static final int[] Lightpink = new int[]{0xff, 0xb6, 0xc1}; // rgb(255.0f, 182, 193)
	public static final int[] Lightsalmon = new int[]{0xff, 0xa0, 0x7a}; // rgb(255.0f, 160, 122)
	public static final int[] Lightseagreen = new int[]{0x20, 0xb2, 0xaa}; // rgb(32, 178, 170)
	public static final int[] Lightskyblue = new int[]{0x87, 0xce, 0xfa}; // rgb(135, 206, 250)
	public static final int[] Lightslategray = new int[]{0x77, 0x88, 0x99}; // rgb(119, 136, 153)
	public static final int[] Lightslategrey = new int[]{0x77, 0x88, 0x99}; // rgb(119, 136, 153)
	public static final int[] Lightsteelblue = new int[]{0xb0, 0xc4, 0xde}; // rgb(176, 196, 222)
	public static final int[] Lightyellow = new int[]{0xff, 0xff, 0xe0}; // rgb(255.0f, 255.0f, 224)
	public static final int[] Lime = new int[]{0x00, 0xff, 0x00}; // rgb(0, 255.0f, 0)
	public static final int[] Limegreen = new int[]{0x32, 0xcd, 0x32}; // rgb(50, 205, 50)
	public static final int[] Linen = new int[]{0xfa, 0xf0, 0xe6}; // rgb(250, 240, 230)
	public static final int[] Magenta = new int[]{0xff, 0x00, 0xff}; // rgb(255.0f, 0, 255.0f)
	public static final int[] Maroon = new int[]{0x80, 0x00, 0x00}; // rgb(128, 0, 0)
	public static final int[] Mediumaquamarine = new int[]{0x66, 0xcd, 0xaa}; // rgb(102, 205, 170)
	public static final int[] Mediumblue = new int[]{0x00, 0x00, 0xcd}; // rgb(0, 0, 205)
	public static final int[] Mediumorchid = new int[]{0xba, 0x55, 0xd3}; // rgb(186, 85, 211)
	public static final int[] Mediumpurple = new int[]{0x93, 0x70, 0xdb}; // rgb(147, 112, 219)
	public static final int[] Mediumseagreen = new int[]{0x3c, 0xb3, 0x71}; // rgb(60, 179, 113)
	public static final int[] Mediumslateblue = new int[]{0x7b, 0x68, 0xee}; // rgb(123, 104, 238)
	public static final int[] Mediumspringgreen = new int[]{0x00, 0xfa, 0x9a}; // rgb(0, 250, 154)
	public static final int[] Mediumturquoise = new int[]{0x48, 0xd1, 0xcc}; // rgb(72, 209, 204)
	public static final int[] Mediumvioletred = new int[]{0xc7, 0x15, 0x85}; // rgb(199, 21, 133)
	public static final int[] Midnightblue = new int[]{0x19, 0x19, 0x70}; // rgb(25, 25, 112)
	public static final int[] Mintcream = new int[]{0xf5, 0xff, 0xfa}; // rgb(245, 255.0f, 250)
	public static final int[] Mistyrose = new int[]{0xff, 0xe4, 0xe1}; // rgb(255.0f, 228, 225)
	public static final int[] Moccasin = new int[]{0xff, 0xe4, 0xb5}; // rgb(255.0f, 228, 181)
	public static final int[] Navajowhite = new int[]{0xff, 0xde, 0xad}; // rgb(255.0f, 222, 173)
	public static final int[] Navy = new int[]{0x00, 0x00, 0x80}; // rgb(0, 0, 128)
	public static final int[] Oldlace = new int[]{0xfd, 0xf5, 0xe6}; // rgb(253, 245, 230)
	public static final int[] Olive = new int[]{0x80, 0x80, 0x00}; // rgb(128, 128, 0)
	public static final int[] Olivedrab = new int[]{0x6b, 0x8e, 0x23}; // rgb(107, 142, 35)
	public static final int[] Orange = new int[]{0xff, 0xa5, 0x00}; // rgb(255.0f, 165, 0)
	public static final int[] Orangered = new int[]{0xff, 0x45, 0x00}; // rgb(255.0f, 69, 0)
	public static final int[] Orchid = new int[]{0xda, 0x70, 0xd6}; // rgb(218, 112, 214)
	public static final int[] Palegoldenrod = new int[]{0xee, 0xe8, 0xaa}; // rgb(238, 232, 170)
	public static final int[] Palegreen = new int[]{0x98, 0xfb, 0x98}; // rgb(152, 251, 152)
	public static final int[] Paleturquoise = new int[]{0xaf, 0xee, 0xee}; // rgb(175, 238, 238)
	public static final int[] Palevioletred = new int[]{0xdb, 0x70, 0x93}; // rgb(219, 112, 147)
	public static final int[] Papayawhip = new int[]{0xff, 0xef, 0xd5}; // rgb(255.0f, 239, 213)
	public static final int[] Peachpuff = new int[]{0xff, 0xda, 0xb9}; // rgb(255.0f, 218, 185)
	public static final int[] Peru = new int[]{0xcd, 0x85, 0x3f}; // rgb(205, 133, 63)
	public static final int[] Pink = new int[]{0xff, 0xc0, 0xcb}; // rgb(255.0f, 192, 203)
	public static final int[] Plum = new int[]{0xdd, 0xa0, 0xdd}; // rgb(221, 160, 221)
	public static final int[] Powderblue = new int[]{0xb0, 0xe0, 0xe6}; // rgb(176, 224, 230)
	public static final int[] Purple = new int[]{0x80, 0x00, 0x80}; // rgb(128, 0, 128)
	public static final int[] Red = new int[]{0xff, 0x00, 0x00}; // rgb(255.0f, 0, 0)
	public static final int[] Rosybrown = new int[]{0xbc, 0x8f, 0x8f}; // rgb(188, 143, 143)
	public static final int[] Royalblue = new int[]{0x41, 0x69, 0xe1}; // rgb(65, 105, 225)
	public static final int[] Saddlebrown = new int[]{0x8b, 0x45, 0x13}; // rgb(139, 69, 19)
	public static final int[] Salmon = new int[]{0xfa, 0x80, 0x72}; // rgb(250, 128, 114)
	public static final int[] Sandybrown = new int[]{0xf4, 0xa4, 0x60}; // rgb(244, 164, 96)
	public static final int[] Seagreen = new int[]{0x2e, 0x8b, 0x57}; // rgb(46, 139, 87)
	public static final int[] Seashell = new int[]{0xff, 0xf5, 0xee}; // rgb(255.0f, 245, 238)
	public static final int[] Sienna = new int[]{0xa0, 0x52, 0x2d}; // rgb(160, 82, 45)
	public static final int[] Silver = new int[]{0xc0, 0xc0, 0xc0}; // rgb(192, 192, 192)
	public static final int[] Skyblue = new int[]{0x87, 0xce, 0xeb}; // rgb(135, 206, 235)
	public static final int[] Slateblue = new int[]{0x6a, 0x5a, 0xcd}; // rgb(106, 90, 205)
	public static final int[] Slategray = new int[]{0x70, 0x80, 0x90}; // rgb(112, 128, 144)
	public static final int[] Slategrey = new int[]{0x70, 0x80, 0x90}; // rgb(112, 128, 144)
	public static final int[] Snow = new int[]{0xff, 0xfa, 0xfa}; // rgb(255.0f, 250, 250)
	public static final int[] Springgreen = new int[]{0x00, 0xff, 0x7f}; // rgb(0, 255.0f, 127)
	public static final int[] Steelblue = new int[]{0x46, 0x82, 0xb4}; // rgb(70, 130, 180)
	public static final int[] Tan = new int[]{0xd2, 0xb4, 0x8c}; // rgb(210, 180, 140)
	public static final int[] Teal = new int[]{0x00, 0x80, 0x80}; // rgb(0, 128, 128)
	public static final int[] Thistle = new int[]{0xd8, 0xbf, 0xd8}; // rgb(216, 191, 216)
	public static final int[] Tomato = new int[]{0xff, 0x63, 0x47}; // rgb(255.0f, 99, 71)
	public static final int[] Turquoise = new int[]{0x40, 0xe0, 0xd0}; // rgb(64, 224, 208)
	public static final int[] Violet = new int[]{0xee, 0x82, 0xee}; // rgb(238, 130, 238)
	public static final int[] Wheat = new int[]{0xf5, 0xde, 0xb3}; // rgb(245, 222, 179)
	public static final int[] White = new int[]{0xff, 0xff, 0xff}; // rgb(255.0f, 255.0f, 255.0f)
	public static final int[] Whitesmoke = new int[]{0xf5, 0xf5, 0xf5}; // rgb(245, 245, 245)
	public static final int[] Yellow = new int[]{0xff, 0xff, 0x00}; // rgb(255.0f, 255.0f, 0)
	public static final int[] Yellowgreen = new int[]{0x9a, 0xcd, 0x32}; // rgb(154, 205, 50)

	public static String colorToHex(int[] color)
	{
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < 3; i++)
		{
			output.append(Integer.toHexString(color[i]));
		}
		return output.toString();
	}

	public static String addColorTag(Object object, int[] color)
	{
		return Messages.format("<col={}>{}</col>", colorToHex(color).toLowerCase(), object);
	}

	/**
	 * Strip color tags from a string.
	 *
	 * @param str input to strip colors from and return
	 * @return words without colors
	 */
	@VisibleForTesting
	public static String getBetweenColors(String str)
	{
		final Pattern pattern = Pattern.compile("<col=[0-9a-f]+>(.+?)</col>");
		final Matcher matcher = pattern.matcher(str);
		boolean b = matcher.find();
		if (b)
		{
			return matcher.group(1);
		}
		return str;
	}

	/**
	 * Strip color tags from a string.
	 *
	 * @param str name to
	 * @return String
	 */
	public static String stripColor(String str)
	{
		return str.replaceAll("(<col=[0-9a-f]+>|</col>)", "");
	}
}
