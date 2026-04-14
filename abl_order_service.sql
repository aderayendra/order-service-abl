-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: host.docker.internal
-- Generation Time: Apr 14, 2026 at 01:54 PM
-- Server version: 10.11.14-MariaDB-0ubuntu0.24.04.1
-- PHP Version: 8.3.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `abl_order_service`
--

-- --------------------------------------------------------

--
-- Table structure for table `produk-order`
--

CREATE TABLE `produk-order` (
  `id` int(11) NOT NULL,
  `produk_id` varchar(10) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `total` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `produk-order`
--

INSERT INTO `produk-order` (`id`, `produk_id`, `jumlah`, `tanggal`, `total`) VALUES
(2, 'P001', 15, '2026-03-15', 225000),
(3, 'P001', 10, '2026-03-15', 150000),
(5, 'P001', 10, '2026-03-15', 150000),
(6, 'P001', 20, '2026-03-15', 250000),
(7, 'P001', 20, '2026-03-15', 250000),
(8, 'P001', 20, '2026-03-15', 250000),
(9, 'P001', 21, '2026-03-15', 270000),
(10, 'P001', 21, '2026-03-15', 270000),
(11, 'P002', 1, '2026-03-15', 300000),
(12, 'P002', 1, '2026-03-15', 300000),
(13, 'P002', 1, '2026-03-15', 300000);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `produk-order`
--
ALTER TABLE `produk-order`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `produk-order`
--
ALTER TABLE `produk-order`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
