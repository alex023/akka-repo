package define

//联盟成员在联盟中的任命
object Appointment extends Enumeration {
  type Appointment = Value
  val ClanAppointmentPrep = Value(0, "prep")

  val ClanAppointmentNormal = Value(10, "Normal")

  val ClanAppointmentElder = Value(50, "Elder")

  val ClanAppointmentOwner = Value(100, "Owner")
}
