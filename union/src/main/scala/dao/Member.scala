package dao

import define.Appointment

//联盟成员
case class Member(accountId: String,
                  clanId: String,
                  Lvl: Int,
                  name: String,
                  medal: Int,
                  appointment: Appointment.Value,
                  donationNum: Int)
