/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.bbn.openmap.dataAccess.image.ImageTile;
import com.bbn.openmap.layer.OMGraphicHandlerLayer;
import com.bbn.openmap.omGraphics.OMGraphicList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author root
 */
public class RasterLayer extends OMGraphicHandlerLayer
{

  private OMGraphicList raster = new OMGraphicList();

  public RasterLayer()
  {
    setName("Raster Layer");
  }

  /**
   * The prepare method always returns the OMGraphicList to be drawn on the map.
   * We're assuming that movingPoints holds the OMPoints as they are modified.
   * We need to create a separate OMGraphicList to return for painting after we
   * call generate() on the OMGraphics with the current map. If we forget to
   * call generate(), the OMGraphics will not know where they are on the map,
   * and they won't drawn themselves.
   *
   * @return
   * @see com.bbn.openmap.layer.OMGraphicHandlerLayer#prepare()
   */
  @Override
  public synchronized OMGraphicList prepare()
  {
    OMGraphicList ret = new OMGraphicList(getRecordPoints());
    ret.generate(getProjection());
    // The ret list is the one used for painting.
    return ret;
  }

  public void addImage(String filename)
  {
    BufferedImage buffimage;
    try
    {
      buffimage = ImageIO.read(new File(filename));
      // double ullat, double ullon, double lrlat, double lrlon
//      ImageTile img = new ImageTile(29.57, 78.37, 28.57, 79.37, buffimage);
      ImageTile img = new ImageTile(23.83577, 77.9877, 19.27882, 86.85722, buffimage);
      getRecordPoints().add(img);
    }
    catch (IOException ex)
    {
      Logger.getLogger(RasterLayer.class.getName()).log(Level.SEVERE, null, ex);
    }

    doPrepare();
  }

  /**
   * @return the raster
   */
  public OMGraphicList getRecordPoints()
  {
    return raster;
  }

  /**
   * @param recordPoints the raster to set
   */
  public void setRecordPoints(OMGraphicList recordPoints)
  {
    this.raster = recordPoints;
  }
}
