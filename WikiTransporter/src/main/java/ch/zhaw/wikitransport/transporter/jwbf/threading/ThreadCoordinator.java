package ch.zhaw.wikitransport.transporter.jwbf.threading;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.wikitransport.page.Page;
import ch.zhaw.wikitransport.transporter.jwbf.bot.MediaWikiDocletBot;
import ch.zhaw.wikitransport.util.WikiTransporterCfg;

/**
 * This class fulfills the task of coordinating and defining the workload of the
 * different threads.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 * 
 */
public class ThreadCoordinator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadCoordinator.class.getName());
	private List<Page> pageList;
	private ThreadPool threadPool;

	MediaWikiDocletBot bot;
	/**
	 * Default Constructor, please run initCoordinator after creating it.
	 */
	public ThreadCoordinator(List<Page> pageList, MediaWikiDocletBot bot) {
		LOGGER.trace("new ThreadCoordinator created");
		// Build the Queue
		this.pageList = pageList;
		this.bot = bot;
		// Create new Thread Pool
		threadPool = new ThreadPool();
	}

	/**
	 * Mainly splits the whole workload into smaller workloads and divides them
	 * to different threads.
	 */
	public void startCoordinating() {
		PageWriter pw;
		
		LOGGER.trace("Assigning workload to Threads");
		for(Page page: pageList) {
			pw = new PageWriter(bot);
			threadPool.startServing(new PageWriterThread(pw, page));
		}
		
		try {
			threadPool.stopServing();
		} catch (InterruptedException e) {
			LOGGER.error("Thread has being interrupted while sleeping: {}", e);
		}
		
		LOGGER.trace("ThreadCoordinater ended.");
	}
	
	/**
	 * Represents the ThreadPool which contains WikiTransporterThreads
	 * 
	 * @author Christian Dubs
	 * 
	 */
	private class ThreadPool {
		private ExecutorService executorService;

		public ThreadPool() {
			initializeThreadPool();
		}

		/**
		 * Creates a new Thread ExecutorService with a given number of Threads
		 * @param numberOfThreads
		 */
		private void initializeThreadPool() {
			executorService = Executors.newFixedThreadPool(WikiTransporterCfg.NUMBER_OF_THREADS);
			// threadPool = Executors.newCachedThreadPool();
		}

		/**
		 * Starts a Thread in the executorService
		 * 
		 * @param t
		 *            - Basically a WikiTransporterThread.
		 */
		public void startServing(Runnable t) {
			executorService.execute(t);
		}

		/**
		 * Stops all Threads and does a shutdown of the executorService.
		 * @throws InterruptedException
		 */
		public void stopServing() throws InterruptedException {
			executorService.shutdown();
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		}
	}
	
	private class PageWriterThread implements Runnable{
		private PageWriter pw;
		private Page page;
		
		public PageWriterThread(PageWriter pw, Page page){
			this.pw = pw;
			this.page = page;
		}

		@Override
		public void run() {
			//Set the page
			pw.setPage(page);
			// Write the Page
			pw.writeArticleIfPageExists();
		}
	}
}
