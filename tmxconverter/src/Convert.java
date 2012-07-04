import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.core.TileSet;
import tiled.io.TMXMapReader;


public class Convert {

	static TMXMapReader reader = new TMXMapReader();
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String inFile = "/Users/meros/Documents/development/playn-greengrappler/greengrappler/core/src/main/java/com/meros/playn/resources/data/rooms/breaktilelevel.tmx";
		PrintStream ps = new PrintStream(new FileOutputStream(inFile.replace(".tmx", ".txt")));
		

		// TODO Auto-generated method stub
		try {
			Map map = reader.readMap(inFile);
			Vector<MapLayer> layers = map.getLayers();

			int width = 0;
			int height = 0;

			for(int i = 0; i < layers.size(); i++)
			{
				MapLayer layer = layers.get(i);
				if (layer.getName().compareTo("middle") == 0)
				{
					width = layer.getWidth();
					height = layer.getHeight();
				}
			}

			//Write width and height
			ps.println(width);
			ps.println(height);

			HashMap<Integer, Integer> gidtoidMap = new HashMap<Integer, Integer>();

			Vector<TileSet> sets = map.getTileSets();

			int id = 0;

			int tilecount = 0;
			for (int i = 0; i < sets.size(); i++)
			{
				tilecount += sets.get(i).size();
			}

			ps.println(tilecount);

			for (int i = 0; i < sets.size(); i++)
			{
				TileSet set = sets.get(i);

				for (int lid = 0; lid <= set.getMaxTileId(); lid++)
				{
					String imageFilename = set.getTilebmpFile();
					int dataPos = imageFilename.indexOf("/data/");
					imageFilename = imageFilename.substring(dataPos+1);
					ps.println(imageFilename);

					int xi = (lid)%set.getTilesPerRow();
					int yi = (lid)/set.getTilesPerRow();

					int x = xi*(set.getTileSpacing()+set.getTileWidth());
					int y = yi*(set.getTileSpacing()+set.getTileHeight());

					ps.println(x);
					ps.println(y);
					ps.println(set.getTileWidth());
					ps.println(set.getTileHeight());

					Tile tile = set.getTile(lid);
					Properties prop = tile.getProperties();
					boolean collide = false;
					boolean hook = false;

					if (prop.containsKey("collide"))
						collide = prop.getProperty("collide").compareTo("true") == 0;
					if (prop.containsKey("hook"))
						hook = prop.getProperty("hook").compareTo("true") == 0;

					ps.println(collide?1:0);
					ps.println(hook?1:0);

					gidtoidMap.put(tile.getGid(), id++);
				}
			}

			printLayer("background", layers, gidtoidMap, ps, width, height);
			printLayer("middle", layers, gidtoidMap, ps, width, height);
			printLayer("foreground", layers, gidtoidMap, ps, width, height);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}
	private static void printLayer(String aLayerName, Vector<MapLayer> aLayers, HashMap<Integer, Integer> aGidToIdMap, PrintStream aPs, int aWidth, int aHeight) {
		boolean found = false;
		for(int i = 0; i < aLayers.size(); i++)
		{
			MapLayer layer = aLayers.get(i);
			if (layer.getName().compareTo(aLayerName) == 0)
			{
				found = true;
				if (layer instanceof TileLayer)
				{
					TileLayer tLayer = (TileLayer)layer;

					for(int x = 0; x < layer.getWidth(); x++)
					{
						for (int y = 0; y < layer.getHeight(); y++)
						{
							Tile tile = tLayer.getTileAt(x, y);

							if (tile != null)
								aPs.println(aGidToIdMap.get(tile.getGid()));
							else
								aPs.println(-1);
						}
					}
				}

			}
		}

		if (!found)
		{
			for(int x = 0; x < aWidth; x++)
			{
				for (int y = 0; y < aWidth; y++)
				{
					aPs.println(-1);
				}
			}
		}

	}
}
