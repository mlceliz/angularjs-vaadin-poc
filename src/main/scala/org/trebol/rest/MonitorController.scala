package org.trebol.rest

import java.lang.management.ManagementFactory
import java.util.Date
import scala.collection.JavaConversions._
import scala.util.{ Failure, Success, Try }
import org.json4s.DefaultFormats
import org.json4s.Formats
import org.json4s.JObject
import org.json4s.JsonDSL._
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport
import javax.management.ObjectName

class MonitorController extends ScalatraServlet with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  implicit def string2objectName(name: String): ObjectName = new ObjectName(name)

  before() {
    contentType = formats("json")
  }

  get("/system") {
    system
  }

  def system(): JObject = {
    def os = {
      val osBean = ManagementFactory.getOperatingSystemMXBean()
      ("architecture" -> osBean.getArch()) ~
        ("name" -> osBean.getName()) ~
        ("version" -> osBean.getVersion()) ~
        ("processors" -> osBean.getAvailableProcessors())
    }

    def runtime = {
      val rt = ManagementFactory.getRuntimeMXBean()
      ("jvmName" -> rt.getSpecName()) ~
        ("jvmVendor" -> rt.getSpecVendor()) ~
        ("jvmVersion" -> rt.getSpecVersion()) ~
        ("jvmSubVersion" -> rt.getVmVersion()) ~
        ("startTime" -> jsonFormats.dateFormat.format(new Date(rt.getStartTime()))) ~
        ("upTime" -> rt.getUptime()) ~
        ("name" -> rt.getName())
    }

    def memory = {
      val mem = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage()
      ("init" -> mem.getInit()) ~
        ("max" -> mem.getMax()) ~
        ("used" -> mem.getUsed())
    }

    ("date" -> jsonFormats.dateFormat.format(new Date())) ~
      ("os" -> os) ~
      ("runtime" -> runtime) ~
      ("memory" -> memory)
  }
}