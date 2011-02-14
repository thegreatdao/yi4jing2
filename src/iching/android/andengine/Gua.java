package iching.android.andengine;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

public class Gua extends Sprite
{

	public Gua(float pX, float pY, TextureRegion pTextureRegion)
	{
		super(pX, pY, pTextureRegion);
	}

	public Gua(float pX, float pY, TextureRegion pTextureRegion,
			RectangleVertexBuffer pRectangleVertexBuffer)
	{
		super(pX, pY, pTextureRegion, pRectangleVertexBuffer);
	}

	public Gua(float pX, float pY, float pWidth, float pHeight,
			TextureRegion pTextureRegion)
	{
		super(pX, pY, pWidth, pHeight, pTextureRegion);
	}

	public Gua(float pX, float pY, float pWidth, float pHeight,
			TextureRegion pTextureRegion,
			RectangleVertexBuffer pRectangleVertexBuffer)
	{
		super(pX, pY, pWidth, pHeight, pTextureRegion, pRectangleVertexBuffer);
	}

}
