package com.meros.playn.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.meros.playn.core.Music.AbstractSong;
import com.meros.playn.core.micromod.IBXM;
import com.meros.playn.core.micromod.Module;

public class Song implements AbstractSong
{
	private static BlockingQueue<Runnable> myQueue = new ArrayBlockingQueue<Runnable>(10);
	private static Executor executor = new ThreadPoolExecutor(1, 4, 50, TimeUnit.MILLISECONDS, myQueue );

	private Module myModule;
	private IBXM myIbxm;
	private final AudioTrack myAudioTrack;
	private static final int SAMPLE_RATE = 48000;
	private final int myBufferSize;


	private Synth synth;
	
	public Song(InputStream aIs) throws IOException
	{
		loadModule(aIs);
		myBufferSize = 
				8 *
				AudioTrack.getMinBufferSize( 
						SAMPLE_RATE, 
						AudioFormat.CHANNEL_CONFIGURATION_STEREO, 
						AudioFormat.ENCODING_PCM_16BIT );        	      
		myAudioTrack = new AudioTrack(
				AudioManager.STREAM_MUSIC, 
				SAMPLE_RATE,
				AudioFormat.CHANNEL_CONFIGURATION_STEREO,
				AudioFormat.ENCODING_PCM_16BIT,
				myBufferSize,
				AudioTrack.MODE_STREAM);

		play();
	}

	private synchronized void loadModule(InputStream aIs) throws IOException {		
		byte[] moduleData = new byte[ ( int ) aIs.available() ];
		int offset = 0;
		while( offset < moduleData.length ) {
			int len = aIs.read( moduleData, offset, moduleData.length - offset );
			if( len < 0 ) throw new IOException( "Unexpected end of file." );
			offset += len;
		}
		aIs.close();
		myModule = new Module( moduleData );
		myIbxm = new IBXM( myModule, SAMPLE_RATE );
	}

	public void update() {
	}

	private class Data
	{
		public int myBufferSize;
		IBXM myIbxm;
		AudioTrack myAudioTrack;
	}

	private class Synth implements Runnable {

		private Data myData;

		public Synth(Data aData)
		{
			myData = aData;
		}
		@Override
		public void run() {
			byte[] outBuffer = null;
			int outIdx = 0;
			int outOffs = 0;

			int[] buffer = new int[myData.myIbxm.getMixBufferLength()];
			outBuffer = new byte[buffer.length*4];

			while (myData.myAudioTrack.getPlayState() != AudioTrack.PLAYSTATE_STOPPED)
			{
				if (outOffs >= outIdx)
				{
					outOffs = 0;
					outIdx = 0;

					int size = myData.myIbxm.getAudio(buffer);

					for( int mixIdx = 0, mixEnd = size * 2; mixIdx < mixEnd; mixIdx++ ) {
						int ampl = buffer[ mixIdx ];
						if( ampl > 32767 ) ampl = 32767;
						if( ampl < -32768 ) ampl = -32768;
						outBuffer[ outIdx++ ] = ( byte ) ampl;
						outBuffer[ outIdx++ ] = ( byte ) ( ampl >> 8 );
					}
				}

				int writeLen = Math.min(outIdx-outOffs, myData.myBufferSize);
				outOffs += myData.myAudioTrack.write(outBuffer, outOffs, writeLen);
			}		
		}
	}


	public void play() {
		myAudioTrack.play();

		Data data = new Data();
		data.myAudioTrack = myAudioTrack;
		data.myIbxm = myIbxm;
		data.myBufferSize = myBufferSize;

		synth = new Synth(data);
		executor.execute(synth);
	}

	public void stop() {
		myAudioTrack.stop();
	}
}
