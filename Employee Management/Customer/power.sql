-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 03, 2022 at 07:24 PM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `power`
--

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `pay1`
--

CREATE TABLE IF NOT EXISTS `pay1` (
  `pyId` int(6) NOT NULL AUTO_INCREMENT,
  `pAcc` varchar(100) NOT NULL,
  `cName` varchar(200) NOT NULL,
  `pyDate` varchar(50) NOT NULL,
  `amount` varchar(20) NOT NULL,
  PRIMARY KEY (`pyId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
