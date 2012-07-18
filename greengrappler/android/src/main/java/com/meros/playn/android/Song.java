package com.meros.playn.android;

import java.io.IOException;
import java.io.InputStream;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;

import com.meros.playn.core.Music.AbstractSong;
import com.meros.playn.core.micromod.IBXM;
import com.meros.playn.core.micromod.Module;

public class Song implements AbstractSong
{
	private static final int SAMPLE_RATE = 48000;

	private Module myModule;
	private IBXM myIbxm;
	private final AudioTrack myAudioTrack;

	private enum State
	{
		STOPPED,
		PLAYING
	}

	private State myState = State.STOPPED;

	private AudioSynthesisTask synth;

	public Song(InputStream aIs) throws IOException
	{
		loadModule(aIs);
		int minSize = AudioTrack.getMinBufferSize( 44100, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT );        	      
		myAudioTrack = new AudioTrack(
				AudioManager.STREAM_MUSIC, 
				SAMPLE_RATE,
				AudioFormat.CHANNEL_CONFIGURATION_STEREO,
				AudioFormat.ENCODING_PCM_16BIT,
				minSize,
				AudioTrack.MODE_STREAM);

		myAudioTrack.play();

		synth = new AudioSynthesisTask();

		AudioSynthesisTask.Data data = synth.new Data();
		data.myAudioTrack = myAudioTrack;
		data.myIbxm = myIbxm;

		synth.execute(data);

		//AudioFormat audioFormat = new AudioFormat( SAMPLE_RATE, 16, 2, true, true );
		//audioLine = AudioSystem.getSourceDataLine( audioFormat );
		//audioLine.open();
		//audioLine.start();
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

	private class AudioSynthesisTask extends AsyncTask<AudioSynthesisTask.Data, Void, Void> {

		public class Data
		{
			IBXM myIbxm;
			AudioTrack myAudioTrack;
		}

		@Override
		protected Void doInBackground(Data... params) {

			while (params[0].myAudioTrack.getPlayState() != AudioTrack.PLAYSTATE_STOPPED)
			{

				byte[] outBuffer = null;
				int outIdx = 0;
				int outOffs = 0;

				int[] buffer = new int[params[0].myIbxm.getMixBufferLength()];

				outBuffer = new byte[buffer.length*4];
				outOffs = 0;
				outIdx = 0;

				int size = params[0].myIbxm.getAudio(buffer);

				for( int mixIdx = 0, mixEnd = size * 2; mixIdx < mixEnd; mixIdx++ ) {
					int ampl = buffer[ mixIdx ];
					if( ampl > 32767 ) ampl = 32767;
					if( ampl < -32768 ) ampl = -32768;
					outBuffer[ outIdx++ ] = ( byte ) ( ampl >> 8 );
					outBuffer[ outIdx++ ] = ( byte ) ampl;
				}

				int writeLen = 100;//Math.min(outIdx-outOffs, audioTrack.);
				outOffs += params[0].myAudioTrack.write(outBuffer, outOffs, writeLen);
			}

			return null;		
		}
	}


	public void play() {
		myState = State.PLAYING;
		myAudioTrack.play();
	}

	public void stop() {
		myState = State.STOPPED;
		myAudioTrack.pause();
	}
}
