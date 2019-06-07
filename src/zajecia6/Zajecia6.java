/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zajecia6;

import java.io.*;
import java.math.BigDecimal;

/**
 *
 * @author Acer
 */
public class Zajecia6 {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    
    public static void CreateHouseData() throws IOException // zad 1 a)
    {
        /*1. Dany jest file, w którym zapisano (w postaci wewnętrznej) kolejne trójki danych tworzące
            informację o budynkach:
            • String nazwDom;
            • int lKondygn;
            • double price;
            Napisać funkcję zmniejszającą (bezpośrednio w pliku, bez wczytywania całego pliku do
            pamięci) o 10 procent cenę domów
            parterowych i zwracającą jako wartość nazwę domu (nazwDom) o największej liczbie
            kondygnacji (jeśli jest takich więcej, to
            dowolny z nich). Nazwa pliku jest przekazana jako parametr funkcji.*/
        
        RandomAccessFile file = null;
        try{
            file = new RandomAccessFile("zadanie1.data","rw");
            file.writeUTF("Hotel A");
            file.writeInt(1);
            file.writeDouble(12345678.9);
            
            file.writeUTF("Hotel B");
            file.writeInt(3);
            file.writeDouble(2222222.2);
            
            file.writeUTF("Hotel C");
            file.writeInt(1);
            file.writeDouble(55555.55);
            
            file.writeUTF("Hotel D");
            file.writeInt(3);
            file.writeDouble(9999999.99);
            
            file.writeUTF("Hotel E");
            file.writeInt(2);
            file.writeDouble(1000000.0);
        }
        finally{
            if(file != null)
            {
                file.close();
            }
        }
        
    }  
    
    public static void ReadHouseData(String fileName) throws IOException // zad 1 b)
    {
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(fileName, "rw");            
            int maxFloors = 0;
            long houseNameToRead = 0;
            long housePriceToRead = 0;
            
            if(file.length() == 0)
            {
                CreateHouseData();
            }
            
            while(file.getFilePointer() < file.length())
            {        
                long houseNamePos = file.getFilePointer();
                String s = file.readUTF();
                int i = file.readInt();
                long housePricePos = file.getFilePointer();
                double d = file.readDouble();
            
            System.out.printf("%s %d %.2f \n", s, i, d);
            
                if(i >= maxFloors)
                {
                    maxFloors = i;
                    houseNameToRead = houseNamePos;
                    housePriceToRead = housePricePos;
                }
                            
            }
            
            file.seek(housePriceToRead);
            double d = file.readDouble();
            System.out.println(d);
            d = d - (d*0.1); 
            file.seek(housePriceToRead);
            file.writeDouble(d);
            file.seek(houseNameToRead);
            String nazwa = file.readUTF();
            System.out.println("Po Zmianie: " + nazwa +" "+ d);
        }
        finally {
            if(file != null){
                file.close();
            }
        }
            
    }
    
    
    public static void SaveObjects(String nPl) throws IOException{
       /* 2. Dany jest file, w którym zapisano informacje o nieruchomościach w postaci kolejnych
        trójek:
        • String nazwDom;
        • int lKondygn;
        • BigDecimal price;
        Napisać funkcję zwiększającą o 15 procent cenę domów 2-piętrowych i wpisującą Data tych
        domów do pliku tekstowego (Data o jednym domu w jednym wierszu; price po zmianie) i
        zwracającą jako swoją wartość liczbę domów, których cen nie zmieniono. Nazwy plików są
        przekazywane przez parametry funkcji.*/

        ObjectOutputStream pl = null;
        try {
            pl = new ObjectOutputStream(new FileOutputStream(nPl));
            
            House d1 = new House("Dom A", 1, new BigDecimal(999999));
            House d2 = new House("Dom B", 2, new BigDecimal(100000));
            House d3 = new House("Dom C", 3, new BigDecimal(222222));
            House d4 = new House("Dom D", 1, new BigDecimal(1));
            House d5 = new House("Dom E", 2, new BigDecimal(120));
            
            pl.writeObject(d1);
            pl.writeObject(d2);
            pl.writeObject(d3);
            pl.writeObject(d4);
            pl.writeObject(d5);
            
            pl.flush(); 
        } finally{
            if(pl != null)
            {
                pl.close();
            }
        }
        
    } // zad 2 a)
    
    public static void ToTextFile(String fileName, String fileNameOut) throws IOException,ClassNotFoundException{
        ObjectInputStream file = null;
        PrintWriter fileOut = null;
        BigDecimal multi = new BigDecimal("1.15");
        int count = 0;
        
        try {
            file = new ObjectInputStream(new FileInputStream(fileName));
            fileOut = new PrintWriter(new FileWriter(fileNameOut, false));
            
            while(true)
            {
                House d;
                d = (House)file.readObject();
                if (d.countFloors == 2)
                {  
                    fileOut.printf("%s %d %.2f\n", d.name, d.countFloors, d.price.multiply(multi));
                } 
                else 
                {
                    count++;
                }
                d.wyswietl();
            }                 
        } catch(EOFException ex)
        {}
        finally{
            System.out.println(count);
            if(file != null && fileOut != null)
            {
                file.close();
                fileOut.close();
            }
        }
        
    } // zad 2 b)
    
    public static void Compress(String fileNameIn, String fileNameOut) throws IOException
    {
        BufferedReader file = null;
        ObjectOutputStream  fileOut = null;

        try{
            file = new BufferedReader(new FileReader(fileNameIn));
            fileOut = new ObjectOutputStream(new FileOutputStream(fileNameOut));
            int count = 0;
            char a = Character.UNASSIGNED;
            char b = Character.UNASSIGNED;
             
            String line;    
            while((line=file.readLine())!=null)
            {
                for (int i = 0; i < line.length(); i++) 
                {
                    a = line.charAt(i);
                    try
                    {
                        b = line.charAt(i+1);
                    } catch (StringIndexOutOfBoundsException ex)
                    {
                        b = line.charAt(line.length() - 1);
                    }
                    count++;
            
                    if (a != b && a != '\n') 
                    {
                        Data Data = new Data(a,count);
                        fileOut.writeObject(Data);
                        count = 0;
                    }  
                }  
                Data data = new Data(a,count);
                fileOut.writeObject(data);
                count = 0;
                fileOut.writeObject(new Data());
            }
            
            
            fileOut.flush();
        }catch (FileNotFoundException ex)
        {
            System.out.println(ex);
        }
        finally
        {
            if (file != null && fileOut != null)
            {
                file.close();
                fileOut.close();
            }
        }
        
        
    } // zad 3
    
    public static void readObjects(String fileName) throws IOException,ClassNotFoundException{
        ObjectInputStream file = null;
        try {
            file = new ObjectInputStream(new FileInputStream(fileName));
            while(true)
            {
                Data t;
                t = (Data)file.readObject();
                t.Wyswietl();
            }     
        } catch(EOFException ex)
        {
            
        }
        finally{
            if(file != null)
            {
                file.close();
            }
        }
        
    } // zad 3
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // TODO code application logic here
        
        //zad1
        //WczytajDaneDomu("zadanie1.data");
        
        //zad2
        //ZapiszObiekty("Domy.data");
        //DoPlikuText("Domy.data", "NoweDomy.txt");
        
        //zad3  
       Compress("zadanie3.txt","zadanie3.data");
       readObjects("zadanie3.data");
       
        
        
    }
    
    
}

class House implements Serializable
{
        public String name;
        public int countFloors;
        public BigDecimal price;
        
        public House(String _name, int _countFloors, BigDecimal _price)
        {
            this.name = _name;
            this.countFloors = _countFloors;
            this.price = _price;  
        }
        
        public void wyswietl(){
            System.out.printf(name + " " + countFloors + " " + price + "\n"); 
        }
        
    }

class Data implements Serializable
{
    private char character;
    private int count;
    private String nl;
    
    Data(char _znak, int _ilosc)
    {
        this.character = _znak;
        this.count = _ilosc;
    }
    
    Data()
    {
        this.nl = "CR";
        this.count = 1;
    }
    public void Wyswietl()
    {   
        if (nl == null)
        {
            System.out.print(character +""+ count + " ");
        } else 
            System.out.print(nl+ "" + count + "\n");
        
    }
}