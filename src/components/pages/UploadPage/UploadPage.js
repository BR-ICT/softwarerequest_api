import React, { useRef, useState } from "react";
import {
  Button,
  MenuItem,
  FormControl,
  Select,
  InputLabel,
  Box,
} from "@material-ui/core";
import MaterialTable from "material-table";
import { Paper, Grid } from "@material-ui/core";
import CloudUploadIcon from "@mui/icons-material/CloudUpload"; // ใช้ไอคอนสำหรับอัปโหลดไฟล์

import * as XLSX from "xlsx";
import Papa from "papaparse";
import { Alert } from "@material-ui/lab";
import * as uploadstatementActions from "../../../actions/uploadstatement.action";
import { useSelector, useDispatch } from "react-redux";
import {
  red,
  green,
  purple,
  teal,
  deepOrange,
  blueGrey,
  yellow,
} from "@material-ui/core/colors/";

import {
  makeStyles,
  withStyles,
  ThemeProvider,
  createMuiTheme,
  createTheme,
} from "@material-ui/core/styles";

export default function UploadWithDropdown() {
  const dispatch = useDispatch();

  const useStyles = makeStyles((theme) => ({
    root: {
      width: "100%",
      marginTop: 60,
    },
    paper: {
      padding: theme.spacing(2),
      color: "red",
      backgroundColor: "#efe5d1",
      backgroundSize: "cover",
      backgroundPosition: "center",
      width: "100%", // ตั้งค่าความกว้างให้เท่ากับ MaterialTable
      marginBottom: theme.spacing(2), // เพิ่มระยะห่างระหว่าง Paper กับ MaterialTable
    },
    margin: {
      marginTop: "0.4rem",
      marginRight: "0.4rem",
      margin: theme.spacing(0.3),
    },
    extendedIcon: {
      marginRight: theme.spacing(1),
    },
    row: {
      borderLeft: 1,
      borderRight: 1,
      borderBottom: 1,
      borderTop: 1,
      borderColor: "#E0E0E0",
      borderStyle: "solid",
    },
    wrapper: {
      margin: "3px",
      position: "relative",
    },
    buttonSuccess: {
      backgroundColor: green[500],
      "&:hover": {
        backgroundColor: green[700],
      },
    },
    fabProgress: {
      color: green[500],
      position: "absolute",
      top: -6,
      left: -6,
      zIndex: 1,
    },
    buttonProgress: {
      color: green[500],
      position: "absolute",
      top: "50%",
      left: "50%",
      marginTop: -12,
      marginLeft: -12,
    },
  }));
  const classes = useStyles();

  const fileInputRef = useRef(null); // สำหรับเลือกไฟล์
  const [selectedValue, setSelectedValue] = useState(""); // สำหรับเก็บค่าจาก Dropdown
  const [tableData, setTableData] = useState([]); // ข้อมูลสำหรับ MaterialTable
  const [columns, setColumns] = useState([]); // คอลัมน์สำหรับ MaterialTable

  // ฟังก์ชันเมื่อกดปุ่ม upload
  const handleButtonClick = () => {
    fileInputRef.current.click(); // เปิดหน้าต่างเลือกไฟล์
  };

  // ฟังก์ชันจัดการการเลือกไฟล์
  const handleFileChange = (event) => {
    const selectedFile = event.target.files[0];
    if (selectedFile) {
      const fileExtension = selectedFile.name
        .split(".")
        .pop()
        .toLowerCase();

      if (fileExtension === "csv") {
        // อ่านไฟล์ CSV
        Papa.parse(selectedFile, {
          header: true,
          complete: (results) => {
            const data = results.data;
            if (data.length > 0) {
              const columns = Object.keys(data[0]).map((key) => ({
                title: key,
                field: key,
              }));
              setColumns(columns);
              setTableData(data);
            }
          },
        });
      } else if (fileExtension === "xlsx" || fileExtension === "xls") {
        // อ่านไฟล์ Excel
        const reader = new FileReader();
        reader.onload = (e) => {
          const data = new Uint8Array(e.target.result);
          const workbook = XLSX.read(data, { type: "array", codepage: 65001 });
          const sheetName = workbook.SheetNames[0];
          const worksheet = workbook.Sheets[sheetName];
          const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });
          const headers = jsonData[0];
          const rows = jsonData.slice(1);

          if (selectedValue === "SCB_IntraDay") {
            alert("SCB");
            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                const header = headers[index];

                if (header === "Amount") {
                  acc[header] = parseFloat(cell).toFixed(2);
                } else if (header === "Date" && typeof cell === "number") {
                  acc[header] = excelDateToFormattedDate(cell);
                } else if (header === "Time" && typeof cell === "number") {
                  acc[header] = convertTimeToAMPM(cell);
                } else {
                  acc[header] = cell;
                }

                return acc;
              }, {})
            );

            const columns = headers.map((header) => ({
              title: header,
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          } else if (selectedValue === "SCB_Historical") {
            alert("SCB_Historical");
            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];
                //  header = header.replace(/[./ ]/g, "_");

                console.log(header);

                if (header === "Credit Amount") {
                  acc[header] = parseFloat(cell).toFixed(2);
                } else if (header === "Date" && typeof cell === "number") {
                  acc[header] = excelDateToFormattedDate(cell);
                } else if (header === "Time" && typeof cell === "number") {
                  acc[header] = convertTimeToAMPM(cell);
                } else {
                  acc[header] = cell;
                }

                return acc;
              }, {})
            );

            const columns = headers.map((header) => ({
              title: header,
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          } else if (selectedValue === "KBANK_IntraDay") {
            alert("KBANK");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                const header = headers[index];

                if (header === "Deposit") {
                  acc[header] = parseFloat(cell).toFixed(2);
                } else if (header === "Date" && typeof cell === "string") {
                  acc[header] = formatDateToYYYYMMDD(cell);
                } else if (header === "Time" && typeof cell === "string") {
                  acc[header] = convertTo12HourFormat(cell);
                } else {
                  acc[header] = cell;
                }

                return acc;
              }, {})
            );

            const columns = headers.map((header) => ({
              title: header,
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          } else if (selectedValue === "KBANK_Historical") {
            alert("KBANK_HIS");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];
                header = header.replace(/[./ ]/g, "_");

                console.log(header);
                if (header === "Deposit") {
                  acc[header] = parseFloat(cell).toFixed(2);
                } else if (header === "Date" && typeof cell === "string") {
                  acc[header] = formatDateToYYYYMMDD(cell);
                } else if (
                  header === "Time_Eff_Date" &&
                  typeof cell === "string"
                ) {
                  acc[header] = convertTo12HourFormat(cell);
                } else {
                  acc[header] = cell;
                }

                return acc;
              }, {})
            );

            const columns = headers.map((header) => ({
              title: (header = header.replace(/[./ ]/g, "_")),
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          } else if (selectedValue === "BBL_IntraDay") {
            alert("BBL_IntraDay");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];

                // ข้าม header ที่ว่างหรือ undefined
                if (!header || header.trim() === "") {
                  return acc; // ข้าม header ที่ว่าง
                }

                // แปลง header ให้ไม่มีช่องว่าง จุด หรือเครื่องหมายอื่นๆ
                header = header.replace(/[./ ]/g, "_");

                // // ข้าม cell ที่ว่าง
                // if (!cell || cell.trim() === "") {
                //   return acc; // ข้าม cell ที่ว่าง
                // }

                // ตรวจสอบและฟอร์แมตตามประเภทข้อมูล
                if (header === "Value_Date") {
                  acc[header] = convertDateslashToYYYYMMDD2(cell);
                } else if (header === "Tran_Date") {
                  acc[header] = convertDateTimeTo12HourFormat(cell);
                } else if (header === "Credit") {
                  // ตั้งค่าให้เป็น 'xxx'

                  const number =
                    cell && cell.trim() !== ""
                      ? parseFloat(cell.replace(/,/g, "")).toFixed(2) || 0.0
                      : 0.0; // ถ้า cell ว่างหรือเป็น null ให้เป็น 0.00

                  acc[header] = number;
                } else {
                  acc[header] = cell;
                }

                return acc; // ต้องคืนค่า acc เพื่อให้ reduce ทำงานต่อได้
              }, {})
            );

            // กำหนด columns โดยข้าม header ที่ว่างหรือไม่จำเป็น
            const columns = headers
              .filter((header) => header && header.trim() !== "") // ข้าม header ที่ว่าง
              .map((header) => ({
                title: header.replace(/[./ ]/g, "_"),
                field: header.replace(/[./ ]/g, "_"),
              }));

            setColumns(columns);
            setTableData(formattedData);
          } else if (selectedValue === "BBL_Historical") {
            alert("BBL_Historical");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];

                // ข้าม header ที่ว่างหรือ undefined
                if (!header || header.trim() === "") {
                  return acc; // ข้าม header ที่ว่าง
                }

                // แปลง header ให้ไม่มีช่องว่าง จุด หรือเครื่องหมายอื่นๆ
                header = header.replace(/[./ ]/g, "_");

                // ข้าม cell ที่ว่าง
                // if (!cell || cell.trim() === "") {
                //   return acc; // ข้าม cell ที่ว่าง
                // }

                // ตรวจสอบและฟอร์แมตตามประเภทข้อมูล
                if (header === "Value_Date") {
                  acc[header] = convertDateslashToYYYYMMDD2(cell);
                } else if (header === "Tran_Date") {
                  acc[header] = convertDateTimeTo12HourFormat(cell);
                } else if (header === "Credit") {
                  // ตั้งค่าให้เป็น 'xxx'
                  const number =
                    cell && cell.trim() !== ""
                      ? parseFloat(cell.replace(/,/g, "")).toFixed(2) || 0.0
                      : 0.0; // ถ้า cell ว่างหรือเป็น null ให้เป็น 0.00

                  // alert(typeof number);
                  acc[header] = number;
                } else {
                  acc[header] = cell;
                }

                return acc; // ต้องคืนค่า acc เพื่อให้ reduce ทำงานต่อได้
              }, {})
            );

            // กำหนด columns โดยข้าม header ที่ว่างหรือไม่จำเป็น
            const columns = headers
              .filter((header) => header && header.trim() !== "") // ข้าม header ที่ว่าง
              .map((header) => ({
                title: header.replace(/[./ ]/g, "_"),
                field: header.replace(/[./ ]/g, "_"),
              }));

            setColumns(columns);
            setTableData(formattedData);
          }

          /////////////////////  SCB CURRENT ////////////////////
          else if (selectedValue === "SCBCUR_IntraDay") {
            alert("SCBCUR_IntraDay");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                const header = headers[index];

                console.log(header);
                console.log(typeof cell);

                if (header === "Amount") {
                  acc[header] = parseFloat(cell)
                    ? parseFloat(cell).toFixed(2)
                    : "0.00"; // ถ้า cell เป็นค่าว่าง ให้เป็น 0.00
                } else if (header === "Date") {
                  acc[header] = convertDateslashToYYYYMMDD(cell);
                } else if (header === "Time") {
                  acc[header] = convertTo12HourFormatFromNumber(cell);
                } else {
                  acc[header] = cell;
                }

                return acc; // ต้องคืนค่า acc เพื่อให้ reduce ทำงานต่อได้
              }, {})
            );

            const columns = headers.map((header) => ({
              title: header,
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          } else if (selectedValue === "SCBCUR_Historical") {
            alert("SCBCUR_Historical");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];

                header = header.replace(/[./ ]/g, "_");

                console.log(header);
                console.log(typeof cell);

                if (header === "Credit_Amount") {
                  acc[header] = parseFloat(cell)
                    ? parseFloat(cell).toFixed(2)
                    : "0.00"; // ถ้า cell เป็นค่าว่าง ให้เป็น 0.00
                } else if (header === "Date") {
                  acc[header] = convertDateslashToYYYYMMDD2(cell);
                } else if (header === "Time") {
                  acc[header] = formatTimeWithSecondsAndPeriod(cell);
                } else {
                  acc[header] = cell;
                }

                return acc; // ต้องคืนค่า acc เพื่อให้ reduce ทำงานต่อได้
              }, {})
            );

            const columns = headers.map((header) => ({
              title: (header = header.replace(/[./ ]/g, "_")),
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          }

          ///////////////////////////////////////////////////////

          /////////// KBANKCURRENT///////
          else if (selectedValue === "KBANKCUR_IntraDay") {
            alert("KBANKCUR_IntraDay");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                const header = headers[index];

                console.log(header);
                console.log(cell);
                console.log(index);

                if (header === "Deposit") {
                  acc[header] = parseFloat(cell)
                    ? parseFloat(cell).toFixed(2)
                    : "0.00"; // ถ้า cell เป็นค่าว่าง ให้เป็น 0.00
                } else if (header === "Date" && typeof cell === "string") {
                  acc[header] = formatDateToYYYYMMDD(cell);
                } else if (header === "Time" && typeof cell === "string") {
                  acc[header] = convertTo12HourFormat(cell);
                } else {
                  acc[header] = cell;
                }

                return acc; // ต้องคืนค่า acc เพื่อให้ reduce ทำงานต่อได้
              }, {})
            );

            const columns = headers.map((header) => ({
              title: header,
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          }

          ///// CURRENT//////
          else if (selectedValue === "KBANKCUR_Historical") {
            alert("KBANKCUR_Historical");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];

                header = header.replace(/[./ ]/g, "_");

                console.log(header);
                console.log(cell);
                console.log(index);

                if (header === "Deposit") {
                  acc[header] = parseFloat(cell)
                    ? parseFloat(cell).toFixed(2)
                    : "0.00"; // ถ้า cell เป็นค่าว่าง ให้เป็น 0.00
                } else if (header === "Date" && typeof cell === "string") {
                  acc[header] = formatDateToYYYYMMDD(cell);
                } else if (
                  header === "Time_Ent_Date" &&
                  typeof cell === "string"
                ) {
                  acc[header] = convertTo12HourFormat(cell);
                } else {
                  acc[header] = cell;
                }

                return acc; // ต้องคืนค่า acc เพื่อให้ reduce ทำงานต่อได้
              }, {})
            );

            const columns = headers.map((header) => ({
              title: (header = header.replace(/[./ ]/g, "_")),
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          }

          ///////////// BBL CURRENT //////////////
          else if (selectedValue === "BBLCUR_IntraDay") {
            alert("BBLCUR_IntraDay");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];

                // ข้าม header ที่ว่างหรือ undefined
                if (!header || header.trim() === "") {
                  return acc; // ข้าม header ที่ว่าง
                }

                // แปลง header ให้ไม่มีช่องว่าง จุด หรือเครื่องหมายอื่นๆ
                header = header.replace(/[./ ]/g, "_");

                // // ข้าม cell ที่ว่าง
                // if (!cell || cell.trim() === "") {
                //   return acc; // ข้าม cell ที่ว่าง
                // }

                // ตรวจสอบและฟอร์แมตตามประเภทข้อมูล
                if (header === "Value_Date") {
                  acc[header] = convertDateslashToYYYYMMDD2(cell);
                } else if (header === "Tran_Date") {
                  acc[header] = convertDateTimeTo12HourFormat(cell);
                } else if (header === "Credit") {
                  // ตั้งค่าให้เป็น 'xxx'

                  const number =
                    cell && cell.trim() !== ""
                      ? parseFloat(cell.replace(/,/g, "")).toFixed(2) || 0.0
                      : 0.0; // ถ้า cell ว่างหรือเป็น null ให้เป็น 0.00

                  acc[header] = number;
                } else {
                  acc[header] = cell;
                }

                return acc; // ต้องคืนค่า acc เพื่อให้ reduce ทำงานต่อได้
              }, {})
            );

            // กำหนด columns โดยข้าม header ที่ว่างหรือไม่จำเป็น
            const columns = headers
              .filter((header) => header && header.trim() !== "") // ข้าม header ที่ว่าง
              .map((header) => ({
                title: header.replace(/[./ ]/g, "_"),
                field: header.replace(/[./ ]/g, "_"),
              }));

            setColumns(columns);
            setTableData(formattedData);
          }

          ///// CURRENT//////
          else if (selectedValue === "BBLCUR_Historical") {
            alert("BBLCUR_Historical");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];

                // ข้าม header ที่ว่างหรือ undefined
                if (!header || header.trim() === "") {
                  return acc; // ข้าม header ที่ว่าง
                }

                // แปลง header ให้ไม่มีช่องว่าง จุด หรือเครื่องหมายอื่นๆ
                header = header.replace(/[./ ]/g, "_");

                // // ข้าม cell ที่ว่าง
                // if (!cell || cell.trim() === "") {
                //   return acc; // ข้าม cell ที่ว่าง
                // }

                // ตรวจสอบและฟอร์แมตตามประเภทข้อมูล
                if (header === "Value_Date") {
                  acc[header] = convertDateslashToYYYYMMDD2(cell);
                } else if (header === "Tran_Date") {
                  acc[header] = convertDateTimeTo12HourFormat(cell);
                } else if (header === "Credit") {
                  // ตั้งค่าให้เป็น 'xxx'

                  const number =
                    cell && cell.trim() !== ""
                      ? parseFloat(cell.replace(/,/g, "")).toFixed(2) || 0.0
                      : 0.0; // ถ้า cell ว่างหรือเป็น null ให้เป็น 0.00

                  acc[header] = number;
                } else {
                  acc[header] = cell;
                }

                return acc; // ต้องคืนค่า acc เพื่อให้ reduce ทำงานต่อได้
              }, {})
            );

            // กำหนด columns โดยข้าม header ที่ว่างหรือไม่จำเป็น
            const columns = headers
              .filter((header) => header && header.trim() !== "") // ข้าม header ที่ว่าง
              .map((header) => ({
                title: header.replace(/[./ ]/g, "_"),
                field: header.replace(/[./ ]/g, "_"),
              }));

            setColumns(columns);
            setTableData(formattedData);
          }

          ///////////////////////////////////////

          ////////// BILL　PAYMENT/////
          else if (selectedValue === "SCBBill_IntraDay") {
            alert("SCBBill_IntraDay");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];
                header = header.replace(/[./ ]/g, "_");

                console.log(header);
                console.log(typeof cell);
                if (header === "Amount") {
                  acc[header] = parseFloat(cell).toFixed(2);
                } else if (header === "Date" && typeof cell === "number") {
                  acc[header] = convertDateslashToYYYYMMDD(cell);
                  // alert(convertDateslashToYYYYMMDD(cell) + "　" + cell);
                } else if (header === "Time" && typeof cell === "string") {
                  acc[header] = convertTo12HourFormat(cell);
                } else {
                  acc[header] = cell;
                }

                return acc;
              }, {})
            );

            const columns = headers.map((header) => ({
              title: (header = header.replace(/[./ ]/g, "_")),
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          } else if (selectedValue === "SCBBill_Historical") {
            alert("SCBBill_Historical");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];
                header = header.replace(/[./ ]/g, "_");

                console.log(header);
                if (header === "Amount") {
                  acc[header] = parseFloat(cell).toFixed(2);
                } else if (header === "Date" && typeof cell === "number") {
                  acc[header] = convertDateslashToYYYYMMDD(cell);
                } else if (header === "Time" && typeof cell === "string") {
                  acc[header] = convertTo12HourFormat(cell);
                } else {
                  acc[header] = cell;
                }

                return acc;
              }, {})
            );

            const columns = headers.map((header) => ({
              title: (header = header.replace(/[./ ]/g, "_")),
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          } else if (selectedValue === "KBANKBill_IntraDay") {
            alert("KBANKBill_IntraDay");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];
                header = header.replace(/[./ ]/g, "_");

                console.log(header);
                if (header === "Amount") {
                  acc[header] = parseFloat(cell).toFixed(2);
                } else if (
                  header === "Transaction_Date" &&
                  typeof cell === "string"
                ) {
                  acc[header] = formatDateToYYYYMMDD(cell);
                } else if (
                  header === "Transaction_Time" &&
                  typeof cell === "string"
                ) {
                  acc[header] = convertTo12HourFormat(cell);
                } else {
                  acc[header] = cell;
                }

                return acc;
              }, {})
            );

            const columns = headers.map((header) => ({
              title: (header = header.replace(/[./ ]/g, "_")),
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          } else if (selectedValue === "KBANKBill_Historical") {
            alert("KBANKBill_Historical");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];
                header = header.replace(/[./ ]/g, "_");

                console.log(header);
                if (header === "Amount") {
                  acc[header] = parseFloat(cell).toFixed(2);
                } else if (
                  header === "Transaction_Date" &&
                  typeof cell === "string"
                ) {
                  acc[header] = formatDateToYYYYMMDD(cell);
                } else if (
                  header === "Transaction_Time" &&
                  typeof cell === "string"
                ) {
                  acc[header] = convertTo12HourFormat(cell);
                } else {
                  acc[header] = cell;
                }

                return acc;
              }, {})
            );

            const columns = headers.map((header) => ({
              title: (header = header.replace(/[./ ]/g, "_")),
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          }

          ///////////////// QR CODE ////////////////
          else if (selectedValue === "BBLQR_IntraDay") {
            alert("BBLQR_IntraDay");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];
                header = header.replace(/[./ ]/g, "_");

                console.log(header);
                if (header === "AMOUNT") {
                  acc[header] = parseFloat(cell).toFixed(2);
                } else if (header === "PAY_DATE" && typeof cell === "string") {
                  acc[header] = convertDateslashToYYYYMMDD2(cell);
                } else if (header === "PAY_TIME" && typeof cell === "string") {
                  acc[header] = convertTo12HourFormat(cell);
                } else {
                  acc[header] = cell;
                }

                return acc;
              }, {})
            );

            const columns = headers.map((header) => ({
              title: (header = header.replace(/[./ ]/g, "_")),
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          } else if (selectedValue === "BBLQR_Historical") {
            alert("BBLQR_Historical");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];
                header = header.replace(/[./ ]/g, "_");

                console.log(header);
                if (header === "AMOUNT") {
                  acc[header] = parseFloat(cell).toFixed(2);
                } else if (header === "PAY_DATE" && typeof cell === "string") {
                  acc[header] = convertDateslashToYYYYMMDD2(cell);
                } else if (header === "PAY_TIME" && typeof cell === "string") {
                  acc[header] = convertTo12HourFormat(cell);
                } else {
                  acc[header] = cell;
                }

                return acc;
              }, {})
            );

            const columns = headers.map((header) => ({
              title: (header = header.replace(/[./ ]/g, "_")),
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          }
          ///////////////////////////   SCB MMN ////////////////////////
          else if (selectedValue === "SCBMMN_IntraDay") {
            alert("SCBMMN_IntraDay");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];
                header = header.replace(/[./ ]/g, "_");

                console.log(header);
                if (header === "Amount") {
                  acc[header] = parseFloat(cell).toFixed(2);
                } else if (header === "Date" && typeof cell === "number") {
                  acc[header] = convertDateslashToYYYYMMDD(cell);
                } else if (header === "Time" && typeof cell === "string") {
                  acc[header] = convertTo12HourFormat(cell);
                } else {
                  acc[header] = cell;
                }

                return acc;
              }, {})
            );

            const columns = headers.map((header) => ({
              title: (header = header.replace(/[./ ]/g, "_")),
              field: header,
            }));
            setColumns(columns);
            setTableData(formattedData);
          } else if (selectedValue === "SCBMMN_Historical") {
            alert("SCBMMN_Historical");

            const formattedData = rows.map((row) =>
              row.reduce((acc, cell, index) => {
                let header = headers[index];

                // ข้าม header ที่ว่างหรือ undefined
                if (!header || header.trim() === "") {
                  return acc; // ข้าม header ที่ว่าง
                }

                // แปลง header ให้ไม่มีช่องว่าง จุด หรือเครื่องหมายอื่นๆ
                header = header.replace(/[./ ]/g, "_");

                // ข้าม cell ที่ว่าง
                // if (!cell || cell.trim() === "") {
                //   return acc; // ข้าม cell ที่ว่าง
                // }

                // ตรวจสอบและฟอร์แมตตามประเภทข้อมูล
                if (header === "Value_Date") {
                  acc[header] = convertDateslashToYYYYMMDD2(cell);
                } else if (header === "Tran_Date") {
                  acc[header] = convertDateTimeTo12HourFormat(cell);
                } else if (header === "Credit") {
                  // ตั้งค่าให้เป็น 'xxx'
                  const number =
                    cell && cell.trim() !== ""
                      ? parseFloat(cell.replace(/,/g, "")).toFixed(2) || 0.0
                      : 0.0; // ถ้า cell ว่างหรือเป็น null ให้เป็น 0.00

                  // alert(typeof number);
                  acc[header] = number;
                } else {
                  acc[header] = cell;
                }

                return acc; // ต้องคืนค่า acc เพื่อให้ reduce ทำงานต่อได้
              }, {})
            );

            // กำหนด columns โดยข้าม header ที่ว่างหรือไม่จำเป็น
            const columns = headers
              .filter((header) => header && header.trim() !== "") // ข้าม header ที่ว่าง
              .map((header) => ({
                title: header.replace(/[./ ]/g, "_"),
                field: header.replace(/[./ ]/g, "_"),
              }));

            setColumns(columns);
            setTableData(formattedData);

            // const formattedData = rows.map((row) =>
            //   row.reduce((acc, cell, index) => {
            //     let header = headers[index];

            //     header = header.replace(/[./ ]/g, "_");

            //     console.log(header);
            //     if (header === "Amount") {
            //       acc[header] = parseFloat(cell).toFixed(2);
            //     } else if (header === "Date" && typeof cell === "number") {
            //       acc[header] = convertDateslashToYYYYMMDD2(cell);
            //     } else if (header === "Time" && typeof cell === "string") {
            //       acc[header] = convertTo12HourFormat(cell);
            //     } else {
            //       acc[header] = cell;
            //     }

            //     return acc;
            //   }, {})
            // );

            // const columns = headers.map((header) => ({
            //   title: (header = header.replace(/[./ ]/g, "_")),
            //   field: header,
            // }));
            // setColumns(columns);
            // setTableData(formattedData);
          }
        };
        reader.readAsArrayBuffer(selectedFile);
      }
    }
  };

  function convertTo12HourFormat(time) {
    let [hours, minutes, seconds] = time.split(":");
    let period = "AM";

    hours = parseInt(hours);

    if (hours >= 12) {
      period = "PM";
      if (hours > 12) {
        hours -= 12;
      }
    } else if (hours === 0) {
      hours = 12;
    }

    return `${String(hours).padStart(2, "0")}:${minutes}:${seconds} ${period}`;
  }

  function formatTimeWithSecondsAndPeriod(time) {
    // แปลงตัวเลขให้เป็น string และเติมศูนย์นำหน้าถ้าจำนวนหลักน้อยกว่า 4
    let timeString = String(time).padStart(4, "0");

    // แยก hours และ minutes จาก string
    let hours = parseInt(timeString.substring(0, 2), 10);
    let minutes = parseInt(timeString.substring(2, 4), 10);

    let period = "AM";

    // กำหนด AM/PM
    if (hours >= 12) {
      period = "PM";
      if (hours > 12) {
        hours -= 12;
      }
    } else if (hours === 0) {
      hours = 12;
    }

    // แสดงเวลาในรูปแบบ 12 ชั่วโมง และเพิ่ม seconds เป็น "00"
    return `${hours}:${String(minutes).padStart(2, "0")}:00 ${period}`;
  }

  function convertTo12HourFormatFromNumber(time) {
    // แปลง number ให้อยู่ในรูปแบบ string และเติมศูนย์นำหน้าถ้าจำนวนหลักน้อยกว่า 6
    let timeString = String(time).padStart(6, "0");

    // แยก hours, minutes, seconds จาก string
    let hours = parseInt(timeString.substring(0, 2), 10);
    let minutes = parseInt(timeString.substring(2, 4), 10);
    let seconds = parseInt(timeString.substring(4, 6), 10);

    let period = "AM";

    if (hours >= 12) {
      period = "PM";
      if (hours > 12) {
        hours -= 12;
      }
    } else if (hours === 0) {
      hours = 12;
    }

    // แสดงค่าเวลาโดยไม่เติมเลข 0 ข้างหน้า hours แต่จะเติมสำหรับ minutes และ seconds
    return `${hours}:${String(minutes).padStart(2, "0")}:${String(
      seconds
    ).padStart(2, "0")} ${period}`;
  }

  function convert24To12HourFormat(time) {}

  const convertTimeToAMPM = (excelTime) => {
    const totalSeconds = excelTime * 24 * 60 * 60;
    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const seconds = Math.floor(totalSeconds % 60);
    const ampm = hours >= 12 ? "PM" : "AM";
    const hour12 = hours % 12 || 12;

    return `${String(hour12).padStart(2, "0")}:${String(minutes).padStart(
      2,
      "0"
    )}:${String(seconds).padStart(2, "0")} ${ampm}`;
  };

  function convertDateslashToYYYYMMDD(date) {
    if (typeof date === "number") {
      let excelStartDate = new Date(1900, 0, 1); // Excel date เริ่มที่ 1/1/1900
      let jsDate = new Date(
        excelStartDate.getTime() + (date - 2) * 24 * 60 * 60 * 1000
      ); // คำนวณวันที่จาก serial
      date = jsDate.toLocaleDateString("en-US"); // แปลงเป็นรูปแบบ MM/DD/YYYY
    }
    let [month, day, year] = date.split("/");
    return `${year}${month.padStart(2, "0")}${day.padStart(2, "0")}`;
  }

  function convertDateslashToYYYYMMDD2(date) {
    if (typeof date === "number") {
      let excelStartDate = new Date(1900, 0, 1); // Excel date เริ่มที่ 1/1/1900
      let jsDate = new Date(
        excelStartDate.getTime() + (date - 2) * 24 * 60 * 60 * 1000
      ); // คำนวณวันที่จาก serial
      date = jsDate.toLocaleDateString("en-US"); // แปลงเป็นรูปแบบ MM/DD/YYYY
    }
    let [month, day, year] = date.split("/");
    return `${year}${day.padStart(2, "0")}${month.padStart(2, "0")}`;
  }

  const excelDateToFormattedDate = (serial) => {
    const excelStartDate = new Date(1899, 11, 30);
    const days = Math.floor(serial);
    const date = new Date(
      excelStartDate.getTime() + days * 24 * 60 * 60 * 1000
    );

    date.setDate(date.getDate());

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");

    return `${year}${month}${day}`;
  };

  function convertDateTimeTo12HourFormat(dateTimeString) {
    // ตรวจสอบว่า dateTimeString เป็นสตริงและไม่ว่าง
    if (typeof dateTimeString !== "string" || dateTimeString.trim() === "") {
      throw new Error("Invalid dateTimeString"); // แจ้งข้อผิดพลาดถ้าค่าที่ได้รับไม่ถูกต้อง
    }

    //console.log(`Input dateTimeString: "${dateTimeString}"`); // แสดงค่าที่ส่งเข้าไป

    // แยกวันและเวลา
    const parts = dateTimeString.split(" ");
    if (parts.length < 2) {
      throw new Error("Invalid dateTimeString format"); // แจ้งข้อผิดพลาดถ้าไม่สามารถแยกวันและเวลาได้
    }

    const timePart = parts[1]; // ส่วนเวลา

    // แยกชั่วโมง นาที วินาที
    const [hours, minutes, seconds] = timePart.split(":").map(Number);

    // ตรวจสอบค่าชั่วโมง นาที และวินาที
    if (hours === undefined || minutes === undefined || seconds === undefined) {
      throw new Error("Invalid time format"); // แจ้งข้อผิดพลาดถ้าค่าเวลาผิดพลาด
    }

    // สร้างตัวแปรสำหรับจัดการเวลา
    let period = "AM"; // กำหนดค่าเริ่มต้นเป็น AM

    // แปลงเวลาจาก 24 ชั่วโมงเป็น 12 ชั่วโมง
    let convertedHours = hours;
    if (hours >= 12) {
      period = "PM";
      if (hours > 12) {
        convertedHours = hours - 12;
      }
    } else if (hours === 0) {
      convertedHours = 12; // ถ้าเป็น 0 ให้แสดงเป็น 12 AM
    }

    // คืนค่าในรูปแบบ HH:MM:SS AM/PM
    return `${String(convertedHours).padStart(2, "0")}:${String(
      minutes
    ).padStart(2, "0")}:${String(seconds).padStart(2, "0")} ${period}`;
  }

  const formatDateToYYYYMMDD = (dateString) => {
    const months = {
      Jan: "01",
      Feb: "02",
      Mar: "03",
      Apr: "04",
      May: "05",
      Jun: "06",
      Jul: "07",
      Aug: "08",
      Sep: "09",
      Oct: "10",
      Nov: "11",
      Dec: "12",
    };

    const [day, month, year] = dateString.split("-");
    const monthNumber = months[month];

    return `${year}${monthNumber}${day.padStart(2, "0")}`;
  };

  // ฟังก์ชันจัดการการเลือกค่าใน Dropdown
  const handleDropdownChange = (event) => {
    setSelectedValue(event.target.value);
    console.log("ค่าที่เลือก:", event.target.value);
  };

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      width="100%"
      style={{ marginTop: "50px" }}
    >
      <Paper className={classes.paper}>
        <Grid container justifyContent="center" spacing={1}>
          <Grid item xs={12}></Grid>
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            width="80%"
            mb={3}
            flexWrap="wrap"
          >
            {/* Dropdown */}
            <FormControl
              variant="outlined"
              style={{ minWidth: 200, marginRight: "20px" }}
            >
              <InputLabel htmlFor="select-box">เลือกประเภท</InputLabel>
              <Select
                value={selectedValue}
                onChange={handleDropdownChange}
                label="เลือกประเภท"
                inputProps={{
                  name: "type",
                  id: "select-box",
                }}
              >
                <MenuItem value="SCB_IntraDay">SCB Intraday(OK)</MenuItem>
                <MenuItem value="SCB_Historical">SCB Historical(OK)</MenuItem>
                <MenuItem value="KBANK_IntraDay">KBANK Intraday(OK)</MenuItem>
                <MenuItem value="KBANK_Historical">
                  KBANK Historical(OK)
                </MenuItem>
                <MenuItem value="BBL_IntraDay">BBL Intraday(OK)</MenuItem>
                <MenuItem value="BBL_Historical">BBL Historical(OK)</MenuItem>
                <MenuItem disabled>────────────────────────────</MenuItem>
                <MenuItem value="SCBBill_IntraDay">
                  SCB(Bill payment) Intraday(OK)
                </MenuItem>
                <MenuItem value="SCBBill_Historical">
                  SCB(Bill payment) Historical(OK)
                </MenuItem>
                <MenuItem value="KBANKBill_IntraDay">
                  KBANK(Bill payment) Intraday(OK)
                </MenuItem>
                <MenuItem value="KBANKBill_Historical">
                  KBANK(Bill payment) Historical(OK)
                </MenuItem>
                <MenuItem value="BBLBill_IntraDay">
                  BBL(Bill payment) Intraday
                </MenuItem>
                <MenuItem value="BBLBill_Historical">
                  BBL(Bill payment) Historical
                </MenuItem>
                <MenuItem disabled>────────────────────────────</MenuItem>
                <MenuItem value="SCBMMN_IntraDay">SCB(Mae Manee)(OK)</MenuItem>
                <MenuItem value="SCBMMN_Historical">
                  SCB(Mae Manee) Historical
                </MenuItem>
                <MenuItem disabled>────────────────────────────</MenuItem>
                <MenuItem value="SCBCUR_IntraDay">
                  SCB(Current) Intraday(OK)
                </MenuItem>
                <MenuItem value="SCBCUR_Historical">
                  SCB(Current) Historical(OK)
                </MenuItem>
                <MenuItem value="KBANKCUR_IntraDay">
                  KBANK(Current) Intraday(OK)
                </MenuItem>
                <MenuItem value="KBANKCUR_Historical">
                  KBANK(Current) Historical(OK)
                </MenuItem>
                <MenuItem value="BBLCUR_IntraDay">
                  BBL(Current) Intraday(OK)
                </MenuItem>
                <MenuItem value="BBLCUR_Historical">
                  BBL(Current) Historical(OK)
                </MenuItem>
                <MenuItem disabled>────────────────────────────</MenuItem>
                <MenuItem value="BBLQR_IntraDay">
                  BBL(Bill QR Code) Intraday(OK)
                </MenuItem>
                <MenuItem value="BBLQR_Historical">
                  BBL(Bill QR Code) Historical(OK)
                </MenuItem>

                <MenuItem disabled>────────────────────────────</MenuItem>
              </Select>
            </FormControl>

            {/* ปุ่ม Choose File */}
            <Button
              style={{
                width: "150px",
                height: "50px",
                marginRight: "10px",
              }}
              variant="contained"
              size="large"
              color="default"
              onClick={handleButtonClick}
            >
              Choose File
            </Button>

            {/* ปุ่ม UPLOAD */}

            <Button
              variant="contained"
              color="primary"
              startIcon={<CloudUploadIcon />}
              style={{
                backgroundColor: "#84d6e2", // สีฟ้าเหมือนในภาพ
                color: "#ffffff", // สีตัวอักษรขาว
                width: "150px",
                height: "50px",
                marginRight: "10px",
              }}
              onClick={async () => {
                let formData = new FormData();
                alert("upload");
                alert(JSON.stringify(tableData));
                alert(selectedValue);
                //todo  upload

                formData.append("tableData", JSON.stringify(tableData)); // แปลง tableData เป็น JSON String
                formData.append("statemenType", selectedValue);

                await dispatch(
                  uploadstatementActions.uploadstatement(formData)
                );
              }}
            >
              Upload File
            </Button>
            {/* <Button
              style={{
                width: "150px",
                height: "50px",
              }}
              variant="contained"
              size="large"
              onClick={async () => {
                let formData = new FormData();
                alert("upload");
                alert(JSON.stringify(tableData));
                alert(selectedValue);
                //todo  upload

                formData.append("tableData", JSON.stringify(tableData)); // แปลง tableData เป็น JSON String
                formData.append("statemenType", selectedValue);

                await dispatch(
                  uploadstatementActions.uploadstatement(formData)
                );
              }}
            >
              UPLOAD
            </Button> */}

            {/* input file ที่ถูกซ่อน */}
            <input
              type="file"
              ref={fileInputRef}
              style={{ display: "none" }}
              onChange={handleFileChange}
            />
          </Box>

          {/* Material Table */}
          <MaterialTable
            title={selectedValue}
            columns={columns}
            data={tableData}
            options={{
              search: true,
              paging: true,
              pageSize: 15, // กำหนดจำนวนแถวเริ่มต้นเป็น 20
              pageSizeOptions: [20, 50, 100], // ตัวเลือกจำนวนแถวที่สามารถเลือกได้
            }}
            style={{ marginTop: "20px", width: "80%" }}
          />
        </Grid>
      </Paper>
    </Box>
  );
}
