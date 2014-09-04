package com.msquared.stairs;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.bindings.admob.GADAdSizeManager;
import org.robovm.bindings.admob.GADBannerView;
import org.robovm.bindings.admob.GADBannerViewDelegateAdapter;
import org.robovm.bindings.admob.GADRequest;
import org.robovm.bindings.admob.GADRequestError;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.utils.Logger;
import com.msquared.stairs.utils.IActivityRequestHandler;


public class RobovmLauncher extends IOSApplication.Delegate 
	implements IActivityRequestHandler{
	
	 private static final Logger log = new Logger(RobovmLauncher.class.getName(), Application.LOG_DEBUG);
	private GADBannerView adview;
	private boolean adsInitialized = false;
	private IOSApplication iosApplication;
	private static final boolean USE_TEST_DEVICES = true;
	public static final String AD_UNIT_ID = "ca-app-pub-5734601135747352/5800108025";
	
	protected IOSApplication createApplication() {
		IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		config.orientationLandscape = false;
		config.orientationPortrait = true;
		
		iosApplication = new IOSApplication(new Stairs(false, true, this), config);
		return iosApplication;
	}

	public static void main(String[] argv) {
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(argv, null, RobovmLauncher.class);
		pool.close();
	}

	// Override
	public void hide() {
		if (!Stairs.PAID_VERSION) {
			initializeAds();

			final CGSize screenSize = UIScreen.getMainScreen().getBounds()
					.size();
			double screenWidth = screenSize.width();

			final CGSize adSize = adview.getBounds().size();
			double adWidth = adSize.width();
			double adHeight = adSize.height();

			log.debug("Hiding ad. size (width, height): (" + adWidth + ", "
					+ adHeight + ")");

			float bannerWidth = (float) screenWidth;
			float bannerHeight = (float) (bannerWidth / adWidth * adHeight);

			adview.setFrame(new CGRect(0, -bannerHeight, bannerWidth,
					bannerHeight));
		}
	}
	
	// Override
	public void show() {
		if (!Stairs.PAID_VERSION) {
			initializeAds();

			final CGSize screenSize = UIScreen.getMainScreen().getBounds()
					.size();
			double screenWidth = screenSize.width();

			final CGSize adSize = adview.getBounds().size();
			double adWidth = adSize.width();
			double adHeight = adSize.height();

			float bannerWidth = (float) screenWidth;
			float bannerHeight = (float) (bannerWidth / adWidth * adHeight);

			log.debug("Show method: (adWidth, adHeight : screenWidth, screenHeight) "
					+ " ( "
					+ adWidth
					+ ", "
					+ adHeight
					+ " : "
					+ screenSize.width() + ", " + screenSize.height() + ")");

			adview.setFrame(new CGRect((screenWidth / 2) - adWidth / 2, 0,
					bannerWidth, bannerHeight));
		}
	}
	
	public void initializeAds() {
		if (!adsInitialized) {
			log.debug("Initializing ads...");
			
			adsInitialized = true;
			
			adview = new GADBannerView(GADAdSizeManager.smartBannerPortrait());
			adview.setAdUnitID(AD_UNIT_ID);
			UIViewController rootViewController = iosApplication.getUIViewController();
			adview.setRootViewController(rootViewController);
			iosApplication.getUIViewController().getView().addSubview(adview);
			
			final GADRequest request = GADRequest.request();
			if (USE_TEST_DEVICES) {
				final NSArray<?> testDevices = new NSArray<NSObject>(
                        new NSString(GADRequest.GAD_SIMULATOR_ID));
				request.setTestDevices(testDevices);
				log.debug("Test devices: " + request.getTestDevices());
			}
			
			adview.setDelegate(new GADBannerViewDelegateAdapter() {
				// Override
				public void didReceiveAd(GADBannerView view) {
					super.didReceiveAd(view);
					log.debug("didReceiveAd");
				}
				
				// Override
				public void didFailToReceiveAd(GADBannerView view,
						GADRequestError error) {
					super.didFailToReceiveAd(view, error);
					log.debug("didFailToReceiveAd: " + error);
				}
			});
			
			adview.loadRequest(request);
			
			log.debug("Initializing ads complete.");
		}
	}
	
	public void showAds(boolean show) {
		if (!Stairs.PAID_VERSION) {
			initializeAds();

			final CGSize screenSize = UIScreen.getMainScreen().getBounds()
					.size();
			double screenWidth = screenSize.width();

			final CGSize adSize = adview.getBounds().size();
			double adWidth = adSize.width();
			double adHeight = adSize.height();

			float bannerWidth = (float) screenWidth;
			float bannerHeight = (float) (bannerWidth / adWidth * adHeight);

			log.debug("Show method: (adWidth, adHeight : screenWidth, screenHeight) "
					+ " ( "
					+ adWidth
					+ ", "
					+ adHeight
					+ " : "
					+ screenSize.width() + ", " + screenSize.height() + ")");

			if (show) {
				adview.setFrame(new CGRect((screenWidth / 2) - adWidth / 2, 0,
						bannerWidth, bannerHeight));
			} else {
				adview.setFrame(new CGRect(0, -bannerHeight, bannerWidth,
						bannerHeight));
			}
		}
	}
}
