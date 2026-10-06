package br.edu.vendas.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@ApplicationScoped
public class ImagemService {
  public static final int LIMITE = 5 * 1024 * 1024;

  public Path diretorio() {
    String d = System.getenv("VENDAS_UPLOAD_DIR");
    return Path.of(d == null ? System.getProperty("user.home") + "/vendas-imagens" : d)
        .toAbsolutePath();
  }

  public String salvar(InputStream in) throws IOException {
    byte[] b = in.readNBytes(LIMITE + 1);
    if (b.length == 0 || b.length > LIMITE)
      throw new IllegalArgumentException("Envie uma imagem de até 5 MB.");
    String ext = extensao(b);
    String nome = UUID.randomUUID() + "." + ext;
    Files.createDirectories(diretorio());
    Files.write(diretorio().resolve(nome), b, StandardOpenOption.CREATE_NEW);
    return nome;
  }

  static String extensao(byte[] b) {
    if (b.length >= 3 && (b[0] & 255) == 255 && (b[1] & 255) == 216 && (b[2] & 255) == 255)
      return "jpg";
    if (b.length >= 8
        && Arrays.equals(Arrays.copyOf(b, 8), new byte[] {(byte) 137, 80, 78, 71, 13, 10, 26, 10}))
      return "png";
    if (b.length >= 12
        && new String(b, 0, 4, java.nio.charset.StandardCharsets.US_ASCII).equals("RIFF")
        && new String(b, 8, 4, java.nio.charset.StandardCharsets.US_ASCII).equals("WEBP"))
      return "webp";
    throw new IllegalArgumentException("Arquivo inválido. Envie JPG, PNG ou WEBP.");
  }

  public Path localizar(String nome) {
    if (nome == null || !nome.matches("[a-f0-9-]{36}\\.(jpg|png|webp)")) return null;
    Path p = diretorio().resolve(nome).normalize();
    return p.startsWith(diretorio()) ? p : null;
  }

  public void excluir(String nome) {
    Path p = localizar(nome);
    if (p != null)
      try {
        Files.deleteIfExists(p);
      } catch (IOException e) {
        System.getLogger(getClass().getName()).log(System.Logger.Level.WARNING, "Imagem órfã", e);
      }
  }
}
