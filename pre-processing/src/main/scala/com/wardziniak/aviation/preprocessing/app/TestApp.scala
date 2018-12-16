package com.wardziniak.aviation.preprocessing.app

import java.io.ByteArrayInputStream

import com.sksamuel.avro4s.AvroInputStream
import com.wardziniak.aviation.common.serialization.GenericDeserializer

object TestApp extends App {


  val inAirFlightData1 = "{ \"takeOfTimestamp\": 1542578159, \"lastTimeStamp\": 1542584244, \"flightInfo\": [ { \"localization\": { \"latitude\": 48.345, \"longitude\": 11.9647, \"altitude\": 2179.32, \"direction\": 113.0 }, \"speed\": { \"horizontal\": 470.408, \"isGround\": false, \"vertical\": 12.6797 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542578159 }, { \"localization\": { \"latitude\": 48.2373, \"longitude\": 12.622, \"altitude\": 6187.44, \"direction\": 102.0 }, \"speed\": { \"horizontal\": 705.612, \"isGround\": false, \"vertical\": 10.4038 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542578461 }, { \"localization\": { \"latitude\": 48.2373, \"longitude\": 12.622, \"altitude\": 6187.44, \"direction\": 102.0 }, \"speed\": { \"horizontal\": 705.612, \"isGround\": false, \"vertical\": 10.4038 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542578461 }, { \"localization\": { \"latitude\": 48.2373, \"longitude\": 12.622, \"altitude\": 6187.44, \"direction\": 102.0 }, \"speed\": { \"horizontal\": 705.612, \"isGround\": false, \"vertical\": 10.4038 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542578461 }, { \"localization\": { \"latitude\": 48.1667, \"longitude\": 13.488, \"altitude\": 8968.74, \"direction\": 107.0 }, \"speed\": { \"horizontal\": 831.548, \"isGround\": false, \"vertical\": 6.5024 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542578765 }, { \"localization\": { \"latitude\": 47.9717, \"longitude\": 14.3019, \"altitude\": 11186.2, \"direction\": 114.0 }, \"speed\": { \"horizontal\": 811.176, \"isGround\": false, \"vertical\": 4.8768 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542579054 }, { \"localization\": { \"latitude\": 47.7701, \"longitude\": 15.1925, \"altitude\": 11887.2, \"direction\": 108.0 }, \"speed\": { \"horizontal\": 837.104, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542579364 }, { \"localization\": { \"latitude\": 47.7701, \"longitude\": 15.1925, \"altitude\": 11887.2, \"direction\": 108.0 }, \"speed\": { \"horizontal\": 837.104, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542579364 }, { \"localization\": { \"latitude\": 47.7701, \"longitude\": 15.1925, \"altitude\": 11887.2, \"direction\": 108.0 }, \"speed\": { \"horizontal\": 837.104, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542579364 }, { \"localization\": { \"latitude\": 47.5755, \"longitude\": 16.0495, \"altitude\": 11872.0, \"direction\": 108.0 }, \"speed\": { \"horizontal\": 837.104, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542579658 }, { \"localization\": { \"latitude\": 47.5755, \"longitude\": 16.0495, \"altitude\": 11872.0, \"direction\": 108.0 }, \"speed\": { \"horizontal\": 837.104, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542579658 }, { \"localization\": { \"latitude\": 47.3331, \"longitude\": 16.9169, \"altitude\": 11887.2, \"direction\": 113.0 }, \"speed\": { \"horizontal\": 848.216, \"isGround\": false, \"vertical\": -0.32512 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542579958 }, { \"localization\": { \"latitude\": 47.3331, \"longitude\": 16.9169, \"altitude\": 11887.2, \"direction\": 113.0 }, \"speed\": { \"horizontal\": 848.216, \"isGround\": false, \"vertical\": -0.32512 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542579958 }, { \"localization\": { \"latitude\": 47.3331, \"longitude\": 16.9169, \"altitude\": 11887.2, \"direction\": 113.0 }, \"speed\": { \"horizontal\": 848.216, \"isGround\": false, \"vertical\": -0.32512 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542579958 }, { \"localization\": { \"latitude\": 47.0799, \"longitude\": 17.7371, \"altitude\": 11887.2, \"direction\": 114.0 }, \"speed\": { \"horizontal\": 842.66, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542580249 }, { \"localization\": { \"latitude\": 47.0799, \"longitude\": 17.7371, \"altitude\": 11887.2, \"direction\": 114.0 }, \"speed\": { \"horizontal\": 842.66, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542580249 }, { \"localization\": { \"latitude\": 47.0799, \"longitude\": 17.7371, \"altitude\": 11887.2, \"direction\": 114.0 }, \"speed\": { \"horizontal\": 842.66, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542580249 }, { \"localization\": { \"latitude\": 46.8305, \"longitude\": 18.5186, \"altitude\": 11894.8, \"direction\": 115.0 }, \"speed\": { \"horizontal\": 833.4, \"isGround\": false, \"vertical\": -0.32512 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542580529 }, { \"localization\": { \"latitude\": 46.5731, \"longitude\": 19.3014, \"altitude\": 11887.2, \"direction\": 115.0 }, \"speed\": { \"horizontal\": 844.512, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542580819 }, { \"localization\": { \"latitude\": 46.5731, \"longitude\": 19.3014, \"altitude\": 11887.2, \"direction\": 115.0 }, \"speed\": { \"horizontal\": 844.512, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542580819 }, { \"localization\": { \"latitude\": 46.5731, \"longitude\": 19.3014, \"altitude\": 11887.2, \"direction\": 115.0 }, \"speed\": { \"horizontal\": 844.512, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542580819 }, { \"localization\": { \"latitude\": 46.2823, \"longitude\": 20.1581, \"altitude\": 11879.6, \"direction\": 116.0 }, \"speed\": { \"horizontal\": 831.548, \"isGround\": false, \"vertical\": -0.32512 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542581129 }, { \"localization\": { \"latitude\": 46.2823, \"longitude\": 20.1581, \"altitude\": 11879.6, \"direction\": 116.0 }, \"speed\": { \"horizontal\": 831.548, \"isGround\": false, \"vertical\": -0.32512 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542581129 }, { \"localization\": { \"latitude\": 46.2823, \"longitude\": 20.1581, \"altitude\": 11879.6, \"direction\": 116.0 }, \"speed\": { \"horizontal\": 831.548, \"isGround\": false, \"vertical\": -0.32512 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542581129 }, { \"localization\": { \"latitude\": 46.0353, \"longitude\": 20.9803, \"altitude\": 11894.8, \"direction\": 112.0 }, \"speed\": { \"horizontal\": 859.328, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542581424 }, { \"localization\": { \"latitude\": 46.0353, \"longitude\": 20.9803, \"altitude\": 11894.8, \"direction\": 112.0 }, \"speed\": { \"horizontal\": 859.328, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542581424 }, { \"localization\": { \"latitude\": 45.7939, \"longitude\": 21.8227, \"altitude\": 11879.6, \"direction\": 112.0 }, \"speed\": { \"horizontal\": 866.736, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542581721 }, { \"localization\": { \"latitude\": 45.7939, \"longitude\": 21.8227, \"altitude\": 11879.6, \"direction\": 112.0 }, \"speed\": { \"horizontal\": 866.736, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542581721 }, { \"localization\": { \"latitude\": 45.7939, \"longitude\": 21.8227, \"altitude\": 11879.6, \"direction\": 112.0 }, \"speed\": { \"horizontal\": 866.736, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542581721 }, { \"localization\": { \"latitude\": 45.537, \"longitude\": 22.6886, \"altitude\": 11887.2, \"direction\": 113.0 }, \"speed\": { \"horizontal\": 872.292, \"isGround\": false, \"vertical\": 0.32512 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542582025 }, { \"localization\": { \"latitude\": 45.537, \"longitude\": 22.6886, \"altitude\": 11887.2, \"direction\": 113.0 }, \"speed\": { \"horizontal\": 872.292, \"isGround\": false, \"vertical\": 0.32512 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542582025 }, { \"localization\": { \"latitude\": 45.537, \"longitude\": 22.6886, \"altitude\": 11887.2, \"direction\": 113.0 }, \"speed\": { \"horizontal\": 872.292, \"isGround\": false, \"vertical\": 0.32512 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542582025 }, { \"localization\": { \"latitude\": 45.29, \"longitude\": 23.5007, \"altitude\": 11574.8, \"direction\": 113.0 }, \"speed\": { \"horizontal\": 844.512, \"isGround\": false, \"vertical\": -5.20192 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542582314 }, { \"localization\": { \"latitude\": 45.29, \"longitude\": 23.5007, \"altitude\": 11574.8, \"direction\": 113.0 }, \"speed\": { \"horizontal\": 844.512, \"isGround\": false, \"vertical\": -5.20192 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542582314 }, { \"localization\": { \"latitude\": 45.0507, \"longitude\": 24.2723, \"altitude\": 8061.96, \"direction\": 113.0 }, \"speed\": { \"horizontal\": 807.472, \"isGround\": false, \"vertical\": -16.256 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542582603 }, { \"localization\": { \"latitude\": 45.0507, \"longitude\": 24.2723, \"altitude\": 8061.96, \"direction\": 113.0 }, \"speed\": { \"horizontal\": 807.472, \"isGround\": false, \"vertical\": -16.256 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542582603 }, { \"localization\": { \"latitude\": 44.8369, \"longitude\": 24.9414, \"altitude\": 4343.4, \"direction\": 114.0 }, \"speed\": { \"horizontal\": 674.128, \"isGround\": false, \"vertical\": -10.4038 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542582882 }, { \"localization\": { \"latitude\": 44.6735, \"longitude\": 25.4416, \"altitude\": 2369.82, \"direction\": 110.0 }, \"speed\": { \"horizontal\": 481.52, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542583141 }, { \"localization\": { \"latitude\": 44.6735, \"longitude\": 25.4416, \"altitude\": 2369.82, \"direction\": 110.0 }, \"speed\": { \"horizontal\": 481.52, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542583141 }, { \"localization\": { \"latitude\": 44.6735, \"longitude\": 25.4416, \"altitude\": 2369.82, \"direction\": 110.0 }, \"speed\": { \"horizontal\": 481.52, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542583141 }, { \"localization\": { \"latitude\": 44.5475, \"longitude\": 25.8243, \"altitude\": 906.78, \"direction\": 100.0 }, \"speed\": { \"horizontal\": 314.84, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542583431 }, { \"localization\": { \"latitude\": 44.5647, \"longitude\": 26.0772, \"altitude\": 68.58, \"direction\": 83.0 }, \"speed\": { \"horizontal\": 214.832, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"MUC\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"OTP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542583737 }, { \"localization\": { \"latitude\": 44.5647, \"longitude\": 26.0772, \"altitude\": 68.58, \"direction\": 83.0 }, \"speed\": { \"horizontal\": 214.832, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"MUC\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"OTP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542583737 }, { \"localization\": { \"latitude\": 44.5647, \"longitude\": 26.0772, \"altitude\": 68.58, \"direction\": 83.0 }, \"speed\": { \"horizontal\": 214.832, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"MUC\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"OTP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542583737 }, { \"localization\": { \"latitude\": 44.5696, \"longitude\": 26.0908, \"altitude\": 0.0, \"direction\": 264.0 }, \"speed\": { \"horizontal\": 53.708, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"MUC\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"OTP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542584023 }, { \"localization\": { \"latitude\": 44.5696, \"longitude\": 26.0908, \"altitude\": 0.0, \"direction\": 264.0 }, \"speed\": { \"horizontal\": 53.708, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"MUC\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"OTP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542584023 }, { \"localization\": { \"latitude\": 44.5704, \"longitude\": 26.0802, \"altitude\": 0.0, \"direction\": 354.0 }, \"speed\": { \"horizontal\": 0.0, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"MUC\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"OTP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"landed\", \"updated\": 1542584244 } ]}"


  val inAirFlightData = "{ \"takeOfTimestamp\": 1542578159, \"lastTimeStamp\": 1542584244, \"flightInfo\": [ { \"localization\": { \"latitude\": 46.5731, \"longitude\": 19.3014, \"altitude\": 11887.2, \"direction\": 115.0 }, \"speed\": { \"horizontal\": 844.512, \"isGround\": false, \"vertical\": 0.0 }, \"departure\": { \"iata\": \"MUC\", \"icao\": \"EDDM\" }, \"arrival\": { \"iata\": \"OTP\", \"icao\": \"LROP\" }, \"aircraft\": { \"regNumber\": \"S5AAX\", \"icao\": \"A319\", \"icao24\": \"506C2F\", \"iata\": \"A319\" }, \"flightNumber\": { \"iata\": \"LH1656\", \"icao\": \"GEC1656\", \"number\": \"1656\" }, \"airlineCode\": { \"iata\": \"LH\", \"icao\": \"GEC\" }, \"enRoute\": \"en-route\", \"updated\": 1542580819 } ] }"

  import com.wardziniak.aviation.api.model.InAirFlightData
  val deserializer = new GenericDeserializer[InAirFlightData]()

  //val aa = deserializer.deserialize("", inAirFlightData.getBytes)


  val in = new ByteArrayInputStream(inAirFlightData.getBytes)
  val input = AvroInputStream.json[InAirFlightData](in)
  //input.iterator.toList.head
  val dd = input.singleEntity
  //input.singleEntity.get
  dd.get


}
