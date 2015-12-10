package com.chungang.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

	public static void main(String[] args) throws IOException {
		Selector selector = Selector.open();
		
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		InetSocketAddress hostAddress = new InetSocketAddress("localhost", 5454);
		serverSocket.socket().bind(hostAddress);
		serverSocket.configureBlocking(false);
		int ops = serverSocket.validOps();
		
		serverSocket.register(selector, ops);
		while (true) {
			System.out.println("Waiting for select...");
			int noOfKeys = selector.select();
			System.out.println("Number of selected keys: " + noOfKeys);
			
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> iter = selectedKeys.iterator();
			while (iter.hasNext()) {
				SelectionKey ky = iter.next();
				if (ky.isAcceptable()) {
					SocketChannel client = serverSocket.accept();
					client.register(selector, SelectionKey.OP_READ);
					System.out.println("Accepted new connection from client: " + client);
				} else if (ky.isReadable()) {
					SocketChannel client = (SocketChannel) ky.channel();
					ByteBuffer buffer = ByteBuffer.allocate(256);
					client.read(buffer);
					String output = new String(buffer.array()).trim();
					System.out.println("Message read from client: " + output);
					if (output.equals("Bye")) {
						client.close();
						System.out.println("Client messages are complete; close.");
					}
				} else {
					
				}
				iter.remove();
			}
		}
	}
}
