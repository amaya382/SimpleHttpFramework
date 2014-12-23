package simplehttpserver.util

import java.io._

import simplehttpserver.util.Common._

import scala.io.Source
import scala.sys.process.BasicIO

trait UseResources {
  def getStringFromResources(name: String): Option[String] = {
    val cl = getClass.getClassLoader
    for {
      is <- Loan(cl.getResourceAsStream(name))
      isr <- Loan(new InputStreamReader(is))
      br <- Loan(new BufferedReader(isr))
    } yield {
      Iterator.continually(br.readLine()).takeWhile(_ != null).mkString
    }
  }

  def getStringFromFile(path: String): Option[String] = {
    val fullPath = path
    val file = new File(fullPath)

    try {
      if (file.exists && file.isFile)
        Some(Source.fromFile(file).getLines().mkString)
      else
        None
    } catch {
      case _: Throwable =>
        None
    }
  }

  def getByteArrayFromFile(file: File): Array[Byte] = {
    try {
      val out = new ByteArrayOutputStream()
      BasicIO.transferFully(new FileInputStream(file), out)
      out.toByteArray
    } catch {
      case _: Throwable =>
        Array()
    }
  }
}
