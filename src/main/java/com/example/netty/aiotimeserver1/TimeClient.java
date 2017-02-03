package com.example.netty.aiotimeserver1;

import com.example.netty.timeserver1.TimeClientHandle;

/**
 * Created by lichao on 2017/1/28.
 */
public class TimeClient {
  public static void main(String[] args) {
    int port = 8080;
    if (args != null && args.length > 0) {
      try {
        port = Integer.valueOf(args[0]);
      } catch (Exception ex) {

      }
    }

    new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO TimeClient-001").start();
  }
}
