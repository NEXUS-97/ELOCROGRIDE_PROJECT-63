-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 26, 2022 at 02:59 PM
-- Server version: 5.6.12-log
-- PHP Version: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `power`
--
CREATE DATABASE IF NOT EXISTS `power` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `power`;

-- --------------------------------------------------------

--
-- Table structure for table `billing`
--

CREATE TABLE IF NOT EXISTS `billing` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `Account_No` varchar(20) NOT NULL,
  `Name` varchar(35) NOT NULL,
  `Address` varchar(35) NOT NULL,
  `From_Date` date NOT NULL,
  `Previous_Reading` int(15) NOT NULL,
  `To_Date` date NOT NULL,
  `Current_Reading` int(15) NOT NULL,
  `Units` int(15) NOT NULL,
  `Current_amount` double NOT NULL,
  `Previous_amount` double NOT NULL,
  `Total_amount` double NOT NULL,
  `Status` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `billing`
--

INSERT INTO `billing` (`ID`, `Account_No`, `Name`, `Address`, `From_Date`, `Previous_Reading`, `To_Date`, `Current_Reading`, `Units`, `Current_amount`, `Previous_amount`, `Total_amount`, `Status`) VALUES
(1, '1234', 'aaa', 'aaa', '2022-04-01', 123, '2022-04-21', 1234, 34, 12.4, 23.1, 20000, 'Done');

-- --------------------------------------------------------

--
-- Table structure for table `customer1`
--

CREATE TABLE IF NOT EXISTS `customer1` (
  `cID` int(6) NOT NULL AUTO_INCREMENT,
  `cName` varchar(200) NOT NULL,
  `cAddress` varchar(200) NOT NULL,
  `cEmail` varchar(200) NOT NULL,
  `cDate` varchar(200) NOT NULL,
  `pno` varchar(10) NOT NULL,
  PRIMARY KEY (`cID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `customer1`
--

INSERT INTO `customer1` (`cID`, `cName`, `cAddress`, `cEmail`, `cDate`, `pno`) VALUES
(6, 'Sasini', 'Mathara', 'Sasini@hotmail.com', '2022-04-21', '0788748569');

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE IF NOT EXISTS `employee` (
  `eID` int(6) NOT NULL AUTO_INCREMENT,
  `eName` varchar(200) NOT NULL,
  `eAddress` varchar(200) NOT NULL,
  `eEmail` varchar(200) NOT NULL,
  `eDate` varchar(200) NOT NULL,
  `pno` varchar(10) NOT NULL,
  PRIMARY KEY (`eID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`eID`, `eName`, `eAddress`, `eEmail`, `eDate`, `pno`) VALUES
(12, 'Kavishka', 'Galee', 'kavishka1@gmail.com', '2022.04.23', '0786543213');

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE IF NOT EXISTS `payments` (
  `PaymentID` int(11) NOT NULL AUTO_INCREMENT,
  `PaymentDate` date NOT NULL,
  `CustomerName` varchar(30) NOT NULL,
  `AccountNo` varchar(25) NOT NULL,
  `PaymentType` varchar(10) NOT NULL,
  `Amount` varchar(25) NOT NULL,
  PRIMARY KEY (`PaymentID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
