-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 30 يونيو 2025 الساعة 19:30
-- إصدار الخادم: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `maintenance`
--

-- --------------------------------------------------------

--
-- بنية الجدول `client`
--

CREATE TABLE `client` (
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `description` mediumtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- إرجاع أو استيراد بيانات الجدول `client`
--

INSERT INTO `client` (`name`, `phone`, `type`, `description`) VALUES
('hk', 'uh', 'كهربائيات', 'hbj'),
('رؤى', '0917289198', 'تكييف وتبريد', '************************'),
('تنى', 'تنلا', 'كهربائيات', 'اتلااتلاات'),
('hk', 'uh', 'كهربائيات', 'hbj'),
('رؤى', '0917289198', 'تكييف وتبريد', '************************'),
('تنى', 'تنلا', 'كهربائيات', 'اتلااتلاات');

-- --------------------------------------------------------

--
-- بنية الجدول `employee`
--

CREATE TABLE `employee` (
  `user_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- إرجاع أو استيراد بيانات الجدول `employee`
--

INSERT INTO `employee` (`user_name`, `password`) VALUES
('User_electrical', 'User_electrical'),
('user_electronics', 'user_electronics'),
('User_electrical', 'User_electrical'),
('user_electronics', 'user_electronics'),
('User_electrical', 'User_electrical'),
('user_electronics', 'user_electronics'),
('User_electrical', 'User_electrical'),
('user_electronics', 'user_electronics');

-- --------------------------------------------------------

--
-- بنية الجدول `requests`
--

CREATE TABLE `requests` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `description` varchar(1028) NOT NULL,
  `type` varchar(255) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- إرجاع أو استيراد بيانات الجدول `requests`
--

INSERT INTO `requests` (`id`, `name`, `phone`, `description`, `type`, `status`) VALUES
(1, 'ايمن', '976986786', 'ماس كهربائي في قاعة الاجتماعات', 'كهرباء', 0),
(2, 'رؤى', '092435679', 'عطل في كهرباء الحجرة', 'كهرباء', 0),
(3, 'نور', '0924445566', 'عطل في اضاءة المكتب', 'كهرباء', 0),
(6, 'دعاء', '093736352', 'توقف الكيبورد عن الاستجابة في عملية الكتابة', 'صيانة إلكترونيات', 0),
(7, 'احمد', '0920000000', 'توقف الشاشة', 'صيانة إلكترونيات', 0),
(8, 'مراد', '0930000000', 'عطل في ألة اعداد القهوة', 'صيانة إلكترونيات', 0),
(9, 'منار', '0923333333', 'توقف في الطابعة ', 'صيانة إلكترونيات', 0),
(11, 'أحمد', '0912345678', 'تصليح كهرباء في المنزل', 'كهرباء', 0),
(12, 'هيثم', '0910000000', '', 'سباكة', 0),
(13, 'خالد ', '0921111111', 'انقطاع كهرباء', 'كهرباء', 0),
(14, 'سارة', '092222222', 'ماس كهربائي', 'كهرباء', 0),
(15, 'لارا', '0910000000', 'انقطاع كهرباء مفاجئ', 'كهرباء', 0),
(16, 'سامي', '0912222222', 'تسريب ميااه', 'سباكة', 0),
(17, 'محمد', '0945555555', 'إعادة طلاء', 'دهانات وتجديد', 0),
(18, 'كريم', '0910000000', 'عطل في جهاز الكمبيوتر', 'صيانة إلكترونيات', 0),
(19, 'تقوى', '0931111111', 'توقف الجهاز اللوحي', 'صيانة إلكترونيات', 0),
(20, 'حسام', '0931112244', 'عطل في جهاز الطابعة بالاستقبال', 'صيانة إلكترونيات', 0),
(21, 'يوسف', '0941112233', 'مشكلة في اضاءة المكتب', 'كهرباء', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `requests`
--
ALTER TABLE `requests`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `requests`
--
ALTER TABLE `requests`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
