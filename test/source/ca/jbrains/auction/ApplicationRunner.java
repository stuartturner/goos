package ca.jbrains.auction;

import static ca.jbrains.auction.FakeAuctionServer.XMPP_HOSTNAME;
import static ca.jbrains.auction.Main.MainWindow.STATUS_JOINING;
import static ca.jbrains.auction.Main.MainWindow.STATUS_LOST;

import javax.swing.SwingUtilities;

public class ApplicationRunner {
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	private AuctionSniperDriver driver;

	public void startBiddingIn(final FakeAuctionServer auction) {
		Thread thread = new Thread("Test Application") {
			@Override
			public void run() {
				try {
					Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD,
							auction.getItemId());
				} catch (Exception logged) {
					logged.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();

		makeSureAwtIsLoadedBeforeStartingTheDriverOnOSXToStopDeadlock();

		driver = new AuctionSniperDriver(1000);
		driver.showsSniperStatus(STATUS_JOINING);
	}

	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus(STATUS_LOST);
	}

	public void stop() {
		if (driver != null)
			driver.dispose();
	}

	private void makeSureAwtIsLoadedBeforeStartingTheDriverOnOSXToStopDeadlock() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
				}
			});
		} catch (Exception e) {
			throw new RuntimeException("Defect", e);
		}
	}
}
