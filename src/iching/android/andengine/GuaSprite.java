package iching.android.andengine;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class GuaSprite extends Sprite
{

	private int resourceId;
	
	public GuaSprite(float pX, float pY, TextureRegion pTextureRegion, int resourceId)
	{
		super(pX, pY, pTextureRegion);
		this.resourceId = resourceId;
	}

	public int getResourceId()
	{
		return resourceId;
	}

	public void setResourceId(int resourceId)
	{
		this.resourceId = resourceId;
	}

}
