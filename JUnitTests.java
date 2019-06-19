package Classes;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
/*import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;*/

import java.io.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.*;
//nazwa tymczasowa- zmienić!!!!
public class Multi_ClientTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    public Client client;
    @Before
    public void setUpStreams() {
        try{
            new Client(InetAddress.getLocalHost(), 666, "test","test");
        } catch(Exception e) {}
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        client = null; //deletes object
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
//https://github.com/Gold7G/Java_drive/blob/master/JUnitTests.java
    @Test
    public void uncommonFilesTest() throws IOException {
        List<String> all = new ArrayList<>();
        all.add("ala");
        all.add("ma");
        all.add("kota");
        List<String> without = new ArrayList<>();
        without.add("ala");
        List<String> expected = new ArrayList<>();
        expected.add("ma");
        expected.add("kota");
        Multi_Client mc = new Multi_Client(new ServerSocket(1),new Socket("localhost",1));
        assertEquals(mc.uncommon_files(all,without),expected);
    }

    @Test
    public void getFileNameTest(){
        Discs disc = new Discs();
        assertEquals(disc.get_file_name("ala\\ma\\kota\\test.txt"),"test.txt");
    }

    @Test
    public void getFileListTest(){
        MyMap test = ControlClient.get_file_list("tests","test");
        List<String> list= new ArrayList<>();
        list.add("test1.txt");
        list.add("test2.txt");
        MyMap expected = new MyMap();
        expected.put("test",list);
        assertEquals(test,expected);
    }

    @Test
    public void DiscsNullFilePathTest(){
        try{
            new Discs("test","client",null, new PrintWriter(System.out));
            fail("Expected exceptio due to null file path");
        } catch (Exception e) {}
    }

    /*//działają
    @Test
    public void ClientSocketTests() {
        try{
            assertEquals(client.socket, new Socket(InetAddress.getLocalHost(),666));
        } catch (Exception e){}
    }
    @Test
    public void ClientClientNameTests() {
        try{
            assertEquals(client.client, "test");
        } catch (Exception e){}
    }
    @Test
    public void ClientFolderPathTests() {
        try{
            assertEquals(client.local_folder_path, "test");
        } catch (Exception e){}
    }
    @Test
    public void DelayTests() {
        try{
            Discs.delay(1);
        } catch (Exception e){}
    }*/


    /*@Test
    public void nullClientNameTest() {
        try {
            new Client(InetAddress.getLocalHost(), 1, null,"test");
        } catch (Exception e) {
            System.out.println("Excepted exception for null path to local folder");
        }
    }*/



}
