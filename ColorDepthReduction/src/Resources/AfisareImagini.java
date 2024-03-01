package Resources;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class AfisareImagini {

	protected HashMap<String, BufferedImage> imagini;//folosesc un HashMap pentru a incarca imaginile din fisier
	protected static JFrame imagesWindow; //folosesc un JFrame pentru a afisa imaginile din fisier si o declar de tip static pentru a avea aceeas la ea din orice clasa

	static{
		System.out.println("-----------Incarcare imagini din fisier pentru a fi afisate in interfata grafica.-----------------"); //afisez un mesaj pentru a vedea cand se incarca imaginile din fisier
	}
	public AfisareImagini(){
		String cale = "src/Images"; //calea catre folderul in care se afla imaginile
		imagini = new HashMap<>(); //initializez HashMap-ul
		  File directorImagini = new File(cale); //creez un obiect de tipul File pentru a putea accesa folderul cu imagini
		  File[] listaImagini = directorImagini.listFiles(); //creez un vector de tipul File pentru a putea accesa imaginile din folder

		  if (listaImagini != null) { //verific daca vectorul este gol
	            for (File imagine : listaImagini) { //parcurg vectorul
	                if (imagine.isFile()) { //verific daca elementul curent este fisier
	                    try {
	                        // Citirea imaginii ca BufferedImage
	                        BufferedImage img = ImageIO.read(imagine);
							String imageName = imagine.getName(); // Salvez numele imaginii
							int dotIndex = imageName.lastIndexOf('.'); // Eliminarea extensiei imaginii
							if (dotIndex > 0) { // Verific dacă extensia imaginii există
								imageName = imageName.substring(0, dotIndex); // Eliminarea extensiei imaginii
							}
							imagini.put(imageName, img); // Adăugarea imaginii în vectorul de BufferedImages
	                    } catch (IOException e) {
	                        e.printStackTrace();//afisez o eroare in cazul in care nu pot citi imaginea
	                    } 
	                }
	            }
	        }
	};

	public void DispunereImagini() //metoda pentru afisarea imaginilor din folderul Images
	{
		  Image[] resizedImages = new Image[imagini.size()]; //creez un vector de tipul Image pentru a putea redimensiona imaginile
			JPanel imaginiPanel = new JPanel(); //creez un JPanel pentru a putea afisa imaginile

			for (Map.Entry<String, BufferedImage> entry : imagini.entrySet()) //parcurg HashMap-ul
		  {
				String imageName = entry.getKey(); // Salvez numele imaginii
				BufferedImage bufferedImage = entry.getValue(); // Salvez imaginea 
				Image resizedImage = bufferedImage.getScaledInstance(300, 200, Image.SCALE_SMOOTH); // Redimensionez imaginea pentru o afisare mai buna in interfata grafica

				JLabel labelImage = new JLabel(imageName); //creez un JLabel pentru a putea afisa numele imaginii
				labelImage.setIcon(new ImageIcon(resizedImage)); //adaug imaginea in JLabel
				labelImage.setBorder(new EmptyBorder(5, 5, 5, 5)); //adaug un border in jurul imaginii
				imaginiPanel.add(labelImage); //adaug JLabel-ul in JPanel
		  }

		  imagesWindow = new JFrame(); //initializez fereastra in care se vor afisa imaginile
		  imagesWindow.setTitle("Imagini fisier"); //setez titlul ferestrei
		  int rows = (int) Math.ceil((double) resizedImages.length / 2); //calculez cate randuri de imagini voi avea in fereastra
		  imaginiPanel.setLayout(new GridLayout(rows,2)); //setez layout-ul pentru JPanel


			ImageIcon iconita = new ImageIcon("design-process.png"); //setez o imagine pentru iconita ferestrei

			JScrollPane scrollPane = new JScrollPane(imaginiPanel); //creez un JScrollPane pentru a putea scrola daca sunt mai multe imagini in folder
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//setez policy-ul pentru scroll

			JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar(); //creez un JScrollBar pentru a putea scrola verical
			verticalScrollBar.setUnitIncrement(10); //setez incrementul cu care se va face scroll-ul

			imagesWindow.add(scrollPane); //adaug JScrollPane-ul in fereastra
			Dimension size = new Dimension(990, 750); //initializez dimensiunea dorita pentru fereastra
			imagesWindow.setPreferredSize(size);//setez dimensiunea ferestrei
			imagesWindow.pack();
			imagesWindow.setResizable(false);//setez ca fereastra sa nu poata fi redimensionata
			imagesWindow.setLocationRelativeTo(null); // Centrarea ferestrei
			imagesWindow.setVisible(true); // Face fereastra vizibilă
			imagesWindow.setIconImage(iconita.getImage()); //setez iconita ferestrei

			JOptionPane.showMessageDialog(null, "Introduceti in consola numele imaginii pe care o doriti procesata !",
	                "Alegere Imagine", JOptionPane.INFORMATION_MESSAGE); //afisez un mesaj pentru a anunta utilizatorul de urmatorul pas pe care trebuie sa il faca
	}


}


