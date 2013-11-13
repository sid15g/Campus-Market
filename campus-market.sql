-- phpMyAdmin SQL Dump
-- version 3.4.10.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 23, 2013 at 03:38 PM
-- Server version: 5.1.53
-- PHP Version: 5.3.4

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `ebuy`
--

-- --------------------------------------------------------

--
-- Table structure for table `bank`
--

CREATE TABLE IF NOT EXISTS `bank` (
  `TID` int(11) NOT NULL,
  `pin` text NOT NULL,
  `seller` varchar(25) NOT NULL,
  `amount` int(11) NOT NULL,
  `buyer` varchar(25) NOT NULL,
  `PID` int(11) NOT NULL,
  `postType` varchar(25) NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`TID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bidpost`
--

CREATE TABLE IF NOT EXISTS `bidpost` (
  `PID` int(25) NOT NULL,
  `initial` int(11) NOT NULL,
  `rate` int(11) NOT NULL,
  `oldPid` int(11) NOT NULL,
  PRIMARY KEY (`PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bids`
--

CREATE TABLE IF NOT EXISTS `bids` (
  `PID` int(11) NOT NULL,
  `username` varchar(25) NOT NULL,
  `amount` int(11) NOT NULL,
  `time` datetime NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `demands`
--

CREATE TABLE IF NOT EXISTS `demands` (
  `PID` int(11) NOT NULL,
  `username` varchar(25) NOT NULL,
  `amount` int(11) NOT NULL,
  `details` text NOT NULL,
  `time` datetime NOT NULL,
  `status` varchar(25) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `posts`
--

CREATE TABLE IF NOT EXISTS `posts` (
  `PID` int(11) NOT NULL,
  `type` varchar(20) NOT NULL,
  `username` varchar(25) NOT NULL,
  `sTime` datetime NOT NULL,
  `eTime` datetime NOT NULL,
  `title` text NOT NULL,
  `details` text NOT NULL,
  `amount` int(11) NOT NULL,
  `status` varchar(25) NOT NULL,
  `category` varchar(50) NOT NULL,
  PRIMARY KEY (`PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `username` varchar(25) NOT NULL,
  `password` text NOT NULL,
  `fullName` text NOT NULL,
  `email` text NOT NULL,
  `mobile` bigint(11) NOT NULL,
  `address` text NOT NULL,
  `DOB` datetime NOT NULL,
  `collegeID` varchar(25) NOT NULL,
  `balance` int(11) NOT NULL,
  `transactions` int(11) NOT NULL,
  `verified` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
