package ca.jbrains.auction.test;

import javax.swing.SwingUtilities;

import ca.jbrains.auction.test.Main.MainWindow;
import static ca.jbrains.auction.test.FakeAuctionServer.XMPP_HOSTNAME;
import static ca.jbrains.auction.test.Main.MainWindow.*;

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
                    // No need to do anything
                }
            });
        } catch (Exception wrapped) {
            throw new AssertionError(wrapped);
        }
    }

    public void hasShownSniperIsBidding() {
        driver.showsSniperStatus(MainWindow.STATUS_BIDDING);
    }
}
