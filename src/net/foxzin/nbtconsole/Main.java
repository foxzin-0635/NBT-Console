package net.mcutils.nbt_manual;

import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

public class Main {
	String fileOutput = "output.nbt";
	private void Window() {
		
		URL[] icnArrayTest = {
				Main.class.getResource("/tag_list.png"),
				Main.class.getResource("/tag_base64_array.png"), 
				Main.class.getResource("/tag_byte.png"),
				Main.class.getResource("/tag_squareroot.png"),
				Main.class.getResource("/tag_zlib.png"),
				Main.class.getResource("/tag_compound.png"),
				Main.class.getResource("/tag_double.png")
		};
		
		// Deprecated?
		/*String[] iconsArray = {
				"../res/tag_list.png",
				"../res/tag_base64_array.png", 
				"../res/tag_byte.png",
				"../res/tag_squareroot.png",
				"../res/tag_zlib.png",
				"../res/tag_compound.png",
				"../res/tag_double.png"
		};*/
		
		Random random = new Random();
		int randomIcon = random.nextInt(icnArrayTest.length);
		URL selRandomIcon = icnArrayTest[randomIcon];
		
		JFrame frame = new JFrame("NBT Console");
		frame.setIconImage(new ImageIcon(selRandomIcon).getImage());
		frame.setSize(600, 400);
		frame.getContentPane().setBackground(new Color(15,15,15));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container frameCont = frame.getContentPane();
		
		JTextPane tPane = new JTextPane();
		tPane.setForeground(Color.WHITE);
		tPane.setCaretColor(Color.WHITE);
		tPane.setBackground(new Color(15,15,15));
		frameCont.add(tPane);
		
		
		// Icon Styles
		Style listTag = tPane.addStyle("TAG_List", null);
		StyleConstants.setFontSize(listTag, 16);
		StyleConstants.setForeground(listTag, Color.WHITE);
		ImageIcon iconListTag = new ImageIcon(icnArrayTest[0]);
		Image imageListRescale = iconListTag.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		iconListTag = new ImageIcon(imageListRescale);
		StyleConstants.setIcon(listTag, iconListTag);
		
		Style byteTag = tPane.addStyle("TAG_Byte", null);
		StyleConstants.setFontSize(byteTag, 16);
		StyleConstants.setForeground(byteTag, Color.WHITE);
		ImageIcon iconByteTag = new ImageIcon(icnArrayTest[2]);
		Image imageByteRescale = iconByteTag.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		iconByteTag = new ImageIcon(imageByteRescale);
		StyleConstants.setIcon(byteTag, iconByteTag);
		
		Style compoundTag = tPane.addStyle("TAG_Compound", null);
		StyleConstants.setFontSize(compoundTag, 16);
		StyleConstants.setForeground(compoundTag, Color.WHITE);
		ImageIcon iconCompoundTag = new ImageIcon(icnArrayTest[5]);
		Image imageCompoundRescale = iconCompoundTag.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		iconCompoundTag = new ImageIcon(imageCompoundRescale);
		StyleConstants.setIcon(compoundTag, iconCompoundTag);
		
		Style endTag = tPane.addStyle("TAG_End", null);
		StyleConstants.setFontSize(endTag, 16);
		StyleConstants.setForeground(endTag, Color.WHITE);
		ImageIcon iconEndTag = new ImageIcon(Main.class.getResource("/tag_end.png"));
		Image imageEndRescale = iconEndTag.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		iconEndTag = new ImageIcon(imageEndRescale);
		StyleConstants.setIcon(endTag, iconEndTag);
		
		Style doubleTag = tPane.addStyle("TAG_Double", null);
		StyleConstants.setFontSize(doubleTag, 16);
		StyleConstants.setForeground(doubleTag, Color.WHITE);
		ImageIcon iconDoubleTag = new ImageIcon(icnArrayTest[6]);
		Image imageDoubleRescale = iconDoubleTag.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		iconDoubleTag = new ImageIcon(imageDoubleRescale);
		StyleConstants.setIcon(doubleTag, iconDoubleTag);
		
		Style configStyle = tPane.addStyle("config", null);
		StyleConstants.setFontSize(configStyle, 16);
		StyleConstants.setForeground(configStyle, Color.WHITE);
		ImageIcon iconConfig = new ImageIcon(Main.class.getResource("/settings_icon.png"));
		Image imageConfigRescale = iconConfig.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		iconConfig = new ImageIcon(imageConfigRescale);
		StyleConstants.setIcon(configStyle, iconConfig);
		
		// Commands
		// setFileOutput("file.extension")
		Pattern setFileOutput = Pattern.compile("(config setFileOutput\\(\"([a-zA-Z0-9_\\.]+)\"\\))");
		// createListTag("Name", typeOfElement, listLength)
		Pattern addListTag = Pattern.compile("(nbt createListTag\\(\"([a-zA-Z0-9_]*)\", (\\d+), (\\d+)\\))");
		// createByteTag("Name", value, isForList: boolean)
		Pattern addByteTag = Pattern.compile("(nbt createByteTag\\(\"([a-zA-Z0-9_]*)\", (\\d+), (true|false)\\))");
		// createEndTag()
		Pattern addEndTag = Pattern.compile("(nbt createEndTag\\(\\))");
		// createCompoundTag("Name", isForList: boolean)
		Pattern addCompoundTag = Pattern.compile("(nbt createCompoundTag\\(\"([a-zA-Z0-9_]*)\", (true|false)\\))");
		// createDoubleTag("Name", value, isForList: boolean)
		Pattern addDoubleTag = Pattern.compile("(nbt createDoubleTag\\(\"([a-zA-Z0-9_]*)\", ([\\d\\.]+), (true|false)\\))");
		
		// Icon Decoration v0.0.1
		// cfg
		Pattern icon_cfg = Pattern.compile("cfg");
		// tag
		Pattern icon_ListTag = Pattern.compile("\\[9\\]");
		Pattern icon_ByteTag = Pattern.compile("\\[1\\]");
		Pattern icon_CompoundTag = Pattern.compile("\\[10\\]");
		Pattern icon_EndTag = Pattern.compile("\\[0\\]");
		Pattern icon_DoubleTag = Pattern.compile("\\[6\\]");
		
		// New Commands
		Pattern ListTag = Pattern.compile("(\\[9\\]\\(\"([a-zA-Z0-9]*)\", (\\d+), (\\d+)\\))");
		Pattern ByteTag = Pattern.compile("(\\[1\\]\\(\"([a-zA-Z0-9]*)\", (\\d+), (true|false)\\))");
		Pattern CompoundTag = Pattern.compile("(\\[10\\]\\(\"([a-zA-Z0-9]*)\", (true|false)\\))");
		Pattern EndTag = Pattern.compile("(\\[0\\]\\(\\))");
		Pattern DoubleTag = Pattern.compile("(\\[6\\]\\(\"([a-zA-Z0-9]*)\", ([\\d\\.]+), (true|false)\\))");
		
		// cfg Commands
		Pattern setOutputFile = Pattern.compile("(cfg setOutputFile\\(\"([a-zA-Z0-9_\\.]+)\"\\))");
		
		// del Commands
		Pattern deleteFromOffsetPlusSize = Pattern.compile("(del foffnsize\\((\\d+), (\\d+)\\))");
		
		// read Commands
		Pattern readFromOffsetPlusSize = Pattern.compile("(read foffnsize\\((\\d+), (\\d+)\\))");
		
		// overwrite
		Pattern overwriteFromOffset = Pattern.compile("(overwrite fromOffset\\((\\d+), (\\d+)\\))");
		Document doc = tPane.getDocument();
		doc.addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				update();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			private void update() {
				
				Runnable test = new Runnable() {

					@Override
					public void run() {
						// New Icons
						Matcher ListTagICN = icon_ListTag.matcher(tPane.getText());
						Matcher ByteTagICN = icon_ByteTag.matcher(tPane.getText());
						Matcher CompoundTagICN = icon_CompoundTag.matcher(tPane.getText());
						Matcher EndTagICN = icon_EndTag.matcher(tPane.getText());
						Matcher DoubleTagICN = icon_DoubleTag.matcher(tPane.getText());
						Matcher configICN = icon_cfg.matcher(tPane.getText());
						
						// New - Icons
						if (configICN.find()) {
							try { 
								doc.remove(configICN.end()-3, 3);
								doc.insertString(0, "cfg", configStyle);
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
						}
						if (ListTagICN.find()) {
							try { 
								doc.remove(ListTagICN.end()-3, 3);
								doc.insertString(0, "[9]", listTag);
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
						}
						
						if (ByteTagICN.find()) {
							try { 
								doc.remove(ByteTagICN.end()-3, 3);
								doc.insertString(0, "[1]", byteTag);
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
						}
						
						if (CompoundTagICN.find()) {
							try { 
								doc.remove(CompoundTagICN.end()-4, 4);
								doc.insertString(0, "[10]", compoundTag);
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
						}
						
						if (EndTagICN.find()) {
							try { 
								doc.remove(EndTagICN.end()-3, 3);
								doc.insertString(0, "[0]", endTag);
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
						}
						
						if (DoubleTagICN.find()) {
							try { 
								doc.remove(DoubleTagICN.end()-3, 3);
								doc.insertString(0, "[6]", doubleTag);
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
						}
					}
				};
				SwingUtilities.invokeLater(test);
			}
		});
		tPane.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					update();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
private void update() {
				
				Runnable test = new Runnable() {

					@Override
					public void run() {
						// New Commands
						Matcher ListTagCMD = ListTag.matcher(tPane.getText());
						Matcher ByteTagCMD = ByteTag.matcher(tPane.getText());
						Matcher CompoundTagCMD = CompoundTag.matcher(tPane.getText());
						Matcher EndTagCMD = EndTag.matcher(tPane.getText());
						Matcher DoubleTagCMD = DoubleTag.matcher(tPane.getText());
						Matcher fileOutputCMD = setOutputFile.matcher(tPane.getText());
						Matcher delDataCMD = deleteFromOffsetPlusSize.matcher(tPane.getText());
						Matcher readDataCMD = readFromOffsetPlusSize.matcher(tPane.getText());
						Matcher overwriteDataCMD = overwriteFromOffset.matcher(tPane.getText());
						
						// Commands - Old
						Matcher addListTagMT = addListTag.matcher(tPane.getText());
						Matcher addByteTagMT = addByteTag.matcher(tPane.getText());
						Matcher addCompoundTagMT = addCompoundTag.matcher(tPane.getText());
						Matcher addEndTagMT = addEndTag.matcher(tPane.getText());
						Matcher addDoubleTagMT = addDoubleTag.matcher(tPane.getText());
						Matcher fileOutputMT = setFileOutput.matcher(tPane.getText());
						
						// New
						// Configs
						if (fileOutputCMD.find()) {
							fileOutput = fileOutputCMD.group(2);
							tPane.setText("");
							System.out.print("File output changed to: \""+fileOutputCMD.group(2)+"\".");
						}
						
						//Reads
						if (readDataCMD.find()) {
							Long startOffs = Long.parseLong(readDataCMD.group(2));
							Integer bytesToRead = Integer.parseInt(readDataCMD.group(3));
							File file = new File(fileOutput);
							try {
								RandomAccessFile raf = new RandomAccessFile(file, "r");
								raf.seek(startOffs);
								
								byte[] buffer = new byte[bytesToRead];
								
								int bytesRead = raf.read(buffer);
								
								if (bytesRead != bytesToRead) {
									System.err.println("Failed to read file.");
								}
								for (int bufferPos = 0; bufferPos < buffer.length; bufferPos++) {
									System.out.println("Data started from: "+startOffs+" length: "+bytesToRead+" position: "+(bufferPos+1)+" is: "+buffer[bufferPos]+".");
								}
								raf.close();
							} catch(IOException e) {
								e.printStackTrace();
							}
							tPane.setText("");
						}
						
						//Overwrites
						if (overwriteDataCMD.find()) {
							Long startOffs = Long.parseLong(overwriteDataCMD.group(2));
							File file = new File(fileOutput);
							try {
								RandomAccessFile raf = new RandomAccessFile(file, "rw");
								
								raf.seek(startOffs);
								
								raf.write(Integer.parseInt(overwriteDataCMD.group(3)));
								
								System.out.println("Byte from offset: "+startOffs+" was overwritten with: "+Integer.parseInt(overwriteDataCMD.group(3))+" successfully!");
								raf.close();
							} catch(IOException e) {
								e.printStackTrace();
							}
							tPane.setText("");
						}
						
						// Dels
						if (delDataCMD.find()) {
							Long startOffs = Long.parseLong(delDataCMD.group(2));
							Long bytesToDel = Long.parseLong(delDataCMD.group(3));
							File file = new File(fileOutput);
							File tempFile = new File(fileOutput + ".temp");
							try {
								FileInputStream fis = new FileInputStream(file);
								FileOutputStream fos = new FileOutputStream(tempFile);
								
								byte[] buffer = new byte[1024];
								int bytesRead;
								long currentPos = 0;
								
								while ((bytesRead = fis.read(buffer)) != -1) {
									long endOfBuffer = currentPos + bytesRead;
									
									if (endOfBuffer <= startOffs || currentPos >= startOffs + bytesToDel) {
										fos.write(buffer, 0, bytesRead);
									} else {
										for (int i = 0; i<bytesRead; i++) {
											long currentBytePosition = currentPos + i;
											if (currentBytePosition < startOffs || currentBytePosition >= startOffs + bytesToDel) {
												fos.write(buffer[i]);
											}
										}
									}
									currentPos = endOfBuffer;
								}
								fos.close();
								fis.close();
								if (file.delete()) {
									if (!tempFile.renameTo(file)) {
										System.err.println("Error on renaming temp file.");
									}
								} else {
									System.err.println("Error on deleting original file.");
								}
							} catch(IOException e) {
								e.printStackTrace();
							}
							tPane.setText("");
							System.out.print("Done! Deleted "+bytesToDel+" bytes started from "+startOffs+".");
						}
						
						if (ListTagCMD.find()) {
							File file = new File(fileOutput);
							if (file.exists() && file.isFile()) {
								try {
									FileOutputStream fos = new FileOutputStream(file, true);
									DataOutputStream dos = new DataOutputStream(fos);
									
									dos.writeByte(9);
									dos.writeShort(ListTagCMD.group(2).length());
									dos.write(ListTagCMD.group(2).getBytes(StandardCharsets.UTF_8));
									dos.writeByte(Integer.parseInt(ListTagCMD.group(3)));
									dos.writeInt(Integer.parseInt(ListTagCMD.group(4)));
									
									fos.close();
									
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								try {
									FileOutputStream fos = new FileOutputStream(file);
									DataOutputStream dos = new DataOutputStream(fos);
									
									dos.writeByte(9);
									dos.writeShort(ListTagCMD.group(2).length());
									dos.write(ListTagCMD.group(2).getBytes(StandardCharsets.UTF_8));
									dos.writeByte(Integer.parseInt(ListTagCMD.group(3)));
									dos.writeInt(Integer.parseInt(ListTagCMD.group(4)));
									
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							tPane.setText("");
							System.out.print("Done!");
						}
						
						if (ByteTagCMD.find()) {
							File file = new File(fileOutput);
							if (file.exists() && file.isFile()) {
								try {
									FileOutputStream fos = new FileOutputStream(file, true);
									DataOutputStream dos = new DataOutputStream(fos);
									
									if (ByteTagCMD.group(4).equals("false")) {
										dos.writeByte(1);
										dos.writeShort(ByteTagCMD.group(2).length());
										dos.write(ByteTagCMD.group(2).getBytes(StandardCharsets.UTF_8));
									}
									if (Integer.parseInt(ByteTagCMD.group(3)) < 127) {
										dos.writeByte(Integer.parseInt(ByteTagCMD.group(3)));
									} else {
										dos.writeByte(127);
									}
									fos.close();
									
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								try {
									FileOutputStream fos = new FileOutputStream(file);
									DataOutputStream dos = new DataOutputStream(fos);
									
									if (ByteTagCMD.group(4).equals("false")) {
										dos.writeByte(1);
										dos.writeShort(ByteTagCMD.group(2).length());
										dos.write(ByteTagCMD.group(2).getBytes(StandardCharsets.UTF_8));
									}
									if (Integer.parseInt(ByteTagCMD.group(3)) < 127) {
										dos.writeByte(Integer.parseInt(ByteTagCMD.group(3)));
									} else {
										dos.writeByte(127);
									}
									
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							tPane.setText("");
							System.out.print("Done!");
						}
						
						if (CompoundTagCMD.find()) {
							File file = new File(fileOutput);
							if (file.exists() && file.isFile()) {
								try {
									FileOutputStream fos = new FileOutputStream(file, true);
									DataOutputStream dos = new DataOutputStream(fos);
									
									if (CompoundTagCMD.group(3).equals("false")) {
										dos.writeByte(10);
										dos.writeShort(CompoundTagCMD.group(2).length());
										dos.write(CompoundTagCMD.group(2).getBytes());
									} else {
										System.err.println("To insert TAG_Compound inside a TAG_List, you need a TAG_End for each compound.");
									}
									
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								try {
									FileOutputStream fos = new FileOutputStream(file);
									DataOutputStream dos = new DataOutputStream(fos);
									
									if (CompoundTagCMD.group(3).equals("false")) {
										dos.writeByte(10);
										dos.writeShort(CompoundTagCMD.group(2).length());
										dos.write(CompoundTagCMD.group(2).getBytes());
									} else {
										System.err.println("To insert TAG_Compound inside a TAG_List, you need a TAG_End for each compound.");
									}
									
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							tPane.setText("");
							System.out.print("Done!");
						}
						
						if (EndTagCMD.find()) {
							File file = new File(fileOutput);
							if (file.exists() && file.isFile()) {
								try {
									FileOutputStream fos = new FileOutputStream(file, true);
									DataOutputStream dos = new DataOutputStream(fos);
									
									dos.writeByte(0);
									
									fos.close();
									
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								try {
									FileOutputStream fos = new FileOutputStream(file);
									DataOutputStream dos = new DataOutputStream(fos);
									
									dos.writeByte(0);
									
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							tPane.setText("");
							System.out.print("Done!");
						}
						
						if (DoubleTagCMD.find()) {
							File file = new File(fileOutput);
							if (file.exists() && file.isFile()) {
								try {
									FileOutputStream fos = new FileOutputStream(file, true);
									DataOutputStream dos = new DataOutputStream(fos);
									
									if (DoubleTagCMD.group(4).equals("false")) {
										dos.writeByte(6);
										dos.writeShort(DoubleTagCMD.group(2).length());
										dos.write(DoubleTagCMD.group(2).getBytes());
									}
									dos.writeDouble(Double.parseDouble(DoubleTagCMD.group(3)));
									
									fos.close();
									
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								try {
									FileOutputStream fos = new FileOutputStream(file);
									DataOutputStream dos = new DataOutputStream(fos);
									
									if (DoubleTagCMD.group(4).equals("false")) {
										dos.writeByte(6);
										dos.writeShort(DoubleTagCMD.group(2).length());
										dos.write(DoubleTagCMD.group(2).getBytes());
									}
									dos.writeDouble(Double.parseDouble(DoubleTagCMD.group(3)));
									
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							tPane.setText("");
							System.out.print("Done!");
						}
						
						// Configs
						if (fileOutputMT.find()) {
							fileOutput = fileOutputMT.group(2);
							tPane.getText().replaceAll("(config setFileOutput\\(\"([a-zA-Z0-9_\\.]+)\\))", "");
							System.out.println("File output changed to: \""+fileOutputMT.group(2)+"\".");
						}
						
						// Old (reference)
						if (addListTagMT.find()) {
							File file = new File(fileOutput);
							tPane.getText().replaceAll("(nbt createListTag\\(\"([a-zA-Z0-9_]*)\", (\\d+), (\\d+)\\))", "");
							if (file.exists() && file.isFile()) {
								try {
									FileOutputStream fos = new FileOutputStream(file, true);
									DataOutputStream dos = new DataOutputStream(fos);
									
									dos.writeByte(9);
									dos.writeShort(addListTagMT.group(2).length());
									dos.write(addListTagMT.group(2).getBytes(StandardCharsets.UTF_8));
									dos.writeByte(Integer.parseInt(addListTagMT.group(3)));
									dos.writeInt(Integer.parseInt(addListTagMT.group(4)));
									
									fos.close();
									
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								try {
									FileOutputStream fos = new FileOutputStream(file);
									DataOutputStream dos = new DataOutputStream(fos);
									
									dos.writeByte(9);
									dos.writeShort(addListTagMT.group(2).length());
									dos.write(addListTagMT.group(2).getBytes(StandardCharsets.UTF_8));
									dos.writeByte(Integer.parseInt(addListTagMT.group(3)));
									dos.writeInt(Integer.parseInt(addListTagMT.group(4)));
									
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						if (addByteTagMT.find()) {
							File file = new File(fileOutput);
							tPane.getText().replaceAll("(nbt createByteTag\\(\"([a-zA-Z0-9_]*)\", (\\d+), (true|false)\\))", "");
							if (file.exists() && file.isFile()) {
								try {
									FileOutputStream fos = new FileOutputStream(file, true);
									DataOutputStream dos = new DataOutputStream(fos);
									
									if (addByteTagMT.group(4).equals("false")) {
										dos.writeByte(1);
										dos.writeShort(addByteTagMT.group(2).length());
										dos.write(addByteTagMT.group(2).getBytes(StandardCharsets.UTF_8));
									}
									if (Integer.parseInt(addByteTagMT.group(3)) < 127) {
										dos.writeByte(Integer.parseInt(addByteTagMT.group(3)));
									} else {
										dos.writeByte(127);
									}
									fos.close();
									
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								try {
									FileOutputStream fos = new FileOutputStream(file);
									DataOutputStream dos = new DataOutputStream(fos);
									
									if (addByteTagMT.group(4).equals("false")) {
										dos.writeByte(1);
										dos.writeShort(addByteTagMT.group(2).length());
										dos.write(addByteTagMT.group(2).getBytes(StandardCharsets.UTF_8));
									}
									if (Integer.parseInt(addByteTagMT.group(3)) < 127) {
										dos.writeByte(Integer.parseInt(addByteTagMT.group(3)));
									} else {
										dos.writeByte(127);
									}
									
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						if (addCompoundTagMT.find()) {
							File file = new File(fileOutput);
							tPane.getText().replaceAll("(nbt createCompoundTag\\(\"([a-zA-Z0-9_]*)\", (true|false)\\))", "");
							if (file.exists() && file.isFile()) {
								try {
									FileOutputStream fos = new FileOutputStream(file, true);
									DataOutputStream dos = new DataOutputStream(fos);
									
									if (addCompoundTagMT.group(3).equals("false")) {
										dos.writeByte(10);
										dos.writeShort(addCompoundTagMT.group(2).length());
										dos.write(addCompoundTagMT.group(2).getBytes());
									} else {
										System.err.println("To insert TAG_Compound inside a TAG_List, you need a TAG_End for each compound.");
									}
									
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								try {
									FileOutputStream fos = new FileOutputStream(file);
									DataOutputStream dos = new DataOutputStream(fos);
									
									if (addCompoundTagMT.group(3).equals("false")) {
										dos.writeByte(10);
										dos.writeShort(addCompoundTagMT.group(2).length());
										dos.write(addCompoundTagMT.group(2).getBytes());
									} else {
										System.err.println("To insert TAG_Compound inside a TAG_List, you need a TAG_End for each compound.");
									}
									
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						if (addEndTagMT.find()) {
							File file = new File(fileOutput);
							tPane.getText().replaceAll("(nbt createEndTag\\(\\))", "");
							if (file.exists() && file.isFile()) {
								try {
									FileOutputStream fos = new FileOutputStream(file, true);
									DataOutputStream dos = new DataOutputStream(fos);
									
									dos.writeByte(0);
									
									fos.close();
									
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								try {
									FileOutputStream fos = new FileOutputStream(file);
									DataOutputStream dos = new DataOutputStream(fos);
									
									dos.writeByte(0);
									
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						if (addDoubleTagMT.find()) {
							File file = new File(fileOutput);
							tPane.getText().replaceAll("(nbt createDoubleTag\\(\"([a-zA-Z0-9_]*)\", ([\\d\\.]+), (true|false)\\))", "");
							if (file.exists() && file.isFile()) {
								try {
									FileOutputStream fos = new FileOutputStream(file, true);
									DataOutputStream dos = new DataOutputStream(fos);
									
									if (addDoubleTagMT.group(4).equals("false")) {
										dos.writeByte(6);
										dos.writeShort(addDoubleTagMT.group(2).length());
										dos.write(addDoubleTagMT.group(2).getBytes());
									}
									dos.writeDouble(Double.parseDouble(addDoubleTagMT.group(3)));
									
									fos.close();
									
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								try {
									FileOutputStream fos = new FileOutputStream(file);
									DataOutputStream dos = new DataOutputStream(fos);
									
									if (addDoubleTagMT.group(4).equals("false")) {
										dos.writeByte(6);
										dos.writeShort(addDoubleTagMT.group(2).length());
										dos.write(addDoubleTagMT.group(2).getBytes());
									}
									dos.writeDouble(Double.parseDouble(addDoubleTagMT.group(3)));
									
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				};
				SwingUtilities.invokeLater(test);
			}
			
		});
		
		frame.setVisible(true);
		
	}
	public static void main(String[] args) {
		new Main().Window();
	}
}
