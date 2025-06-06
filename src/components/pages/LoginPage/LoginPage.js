import React, { useEffect, useState, useMemo } from "react";
import { makeStyles } from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import TextField from "@material-ui/core/TextField";
import Link from "@material-ui/core/Link";
import Grid from "@material-ui/core/Grid";
import Alert from "@material-ui/lab/Alert";
import { Link as RouterLink } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { Formik } from "formik";
import CircularProgress from "@material-ui/core/CircularProgress";
import * as loginActions from "./../../../actions/login.action";
import * as companyActions from "./../../../actions/company.action";
import * as locationActions from "./../../../actions/location.action";

import LinkPDF from "./../../../../src/doc/mar_document.pdf";
import LinkVisitorPDF from "./../../../../src/doc/WELCOME_VISITOR_FORVISITOR.pdf";
import LinkUserPDF from "./../../../../src/doc/WELCOME_VISITOR_FORUSER.pdf";

const useStyles = makeStyles((theme) => ({
  root: {
    maxWidth: 440,
  },
  media: {
    height: 290,
    backgroundSize: "auto",
  },
  textField: {
    width: 200,
  },
  menu: {
    width: 200,
  },
}));

const LoginPage = (props) => {
  const classes = useStyles();
  const dispatch = useDispatch();
  const loginReducer = useSelector(({ loginReducer }) => loginReducer);
  const companyReducer = useSelector(({ companyReducer }) => companyReducer);
  const locationReducer = useSelector(({ locationReducer }) => locationReducer);

  const [company, setCompany] = useState("");
  const [location, setLocation] = useState("");

  const [cono, setCono] = useState("");
  const [divi, setDivi] = useState("");

  const [alertMessage, setAlertMessage] = useState("");
  const [alertDisable, setAlertDisable] = useState(false);

  useEffect(() => {
    dispatch(companyActions.getCompanys());
  }, []);

  const companys = useMemo(() =>
    companyReducer.result ? companyReducer.result : []
  );

  const locations = useMemo(() =>
    locationReducer.result ? locationReducer.result : []
  );

  // const companys = [
  //   {
  //     ID: 1,
  //     CCCONO: 10,
  //     CCDIVI: "101",
  //     CCCONM: "บริษัท บางกอกแร้นช์ จำกัด (มหาชน) ",
  //     COMPANY: '"10 : 101 : บริษัท บางกอกแร้นช์ จำกัด (มหาชน)"',
  //   },
  //   {
  //     ID: 2,
  //     CCCONO: 100,
  //     CCDIVI: "110",
  //     CCCONM: "บริษัท อนาทิส ฟู๊ดส์ จำกัด ",
  //     COMPANY: '"100 : 110 : บริษัท อนาทิส ฟู๊ดส์ จำกัด"',
  //   },
  //   {
  //     ID: 3,
  //     CCCONO: 100,
  //     CCDIVI: "120",
  //     CCCONM: "ANATIS FOODS LIMITED (HK) ",
  //     COMPANY: '"100 : 120 : ANATIS FOODS LIMITED (HK)"',
  //   },
  //   {
  //     ID: 4,
  //     CCCONO: 100,
  //     CCDIVI: "130",
  //     CCCONM: "ANATIS FOODS LIMITED (SG) ",
  //     COMPANY: '"100 : 130 : ANATIS FOODS LIMITED (SG)"',
  //   },
  //   {
  //     ID: 5,
  //     CCCONO: 200,
  //     CCDIVI: "200",
  //     CCCONM: "บริษัท บีอาร์ การเกษตร จำกัด ",
  //     COMPANY: '"200 : 200 : บริษัท บีอาร์ การเกษตร จำกัด"',
  //   },
  //   {
  //     ID: 6,
  //     CCCONO: 300,
  //     CCDIVI: "300",
  //     CCCONM: "บริษัท บีเอ็ม การเกษตร จำกัด ",
  //     COMPANY: '"300 : 300 : บริษัท บีเอ็ม การเกษตร จำกัด"',
  //   },
  //   {
  //     ID: 7,
  //     CCCONO: 400,
  //     CCDIVI: "400",
  //     CCCONM: "บริษัท ฟู้ด ซิตี้ จำกัด ",
  //     COMPANY: '"400 : 400 : บริษัท ฟู้ด ซิตี้ จำกัด"',
  //   },
  //   {
  //     ID: 8,
  //     CCCONO: 500,
  //     CCDIVI: "500",
  //     CCCONM: "บริษัท เอ็นเอส เดลิคาเทสเซน จำกัด ",
  //     COMPANY: '"500 : 500 : บริษัท เอ็นเอส เดลิคาเทสเซน จำกัด"',
  //   },
  //   {
  //     ID: 9,
  //     CCCONO: 600,
  //     CCDIVI: "600",
  //     CCCONM: "บริษัท วินไทย ฟู้ด จำกัด ",
  //     COMPANY: '"600 : 600 : บริษัท วินไทย ฟู้ด จำกัด"',
  //   },
  // ];

  // const locations = [
  //   { ID: 11, CCCONO: 10, LOCATION: "ตึกหน้า " },
  //   { ID: 12, CCCONO: 10, LOCATION: "ตึกข้าง" },
  //   { ID: 33, CCCONO: 600, LOCATION: "บริษัท วินไทย ฟู้ด จำกัด " },
  // ];

  const locationsA = [{ ID: 11, CCCONO: 10, LOCATION: "ตึกหน้า AA " }];
  // add some comment

  const showForm = ({
    values,
    handleChange,
    handleSubmit,
    setFieldValue,
    isSubmitting,
  }) => {
    return (
      <form onSubmit={handleSubmit}>
        <TextField
          variant="outlined"
          margin="normal"
          required
          fullWidth
          id="username"
          label="Username (M3-Add on)"
          placeholder="Username (M3-Add on)"
          onChange={handleChange}
          value={values.username}
          autoFocus
        />
        <TextField
          variant="outlined"
          margin="normal"
          required
          fullWidth
          id="password"
          label="Password"
          onChange={handleChange}
          value={values.password}
          type="password"
        />

        <TextField
          select
          variant="outlined"
          margin="normal"
          required
          fullWidth
          id="company"
          label="Company"
          value={company}
          onChange={
            (handleChange = (event) => {
              // console.log(event.target.value);
              values.company = event.target.value;
              setCompany(event.target.value);

              let text = event.target.value;
              const myArray = text.split(":");
              setCono(myArray[0]);
              setDivi(myArray[1]);

              dispatch(locationActions.getLocation(myArray[0], myArray[1]));
            })
          }
          SelectProps={{
            native: true,
          }}
        >
          <option />
          {companys.map((option) => (
            <option key={option.ID} value={option.COMPANY}>
              {option.COMPANY}
            </option>
          ))}
        </TextField>

        <TextField
          select
          variant="outlined"
          margin="normal"
          required
          fullWidth
          id="location"
          value={location}
          onChange={
            (handleChange = (event) => {
              values.location = event.target.value;
              values.company += " : " + event.target.value;
              setLocation(event.target.value);
            })
          }
          SelectProps={{
            native: true,
          }}
        >
          <option />
          {locations.map((option) => (
            <option key={option.LOCATION} value={option.LOCATION}>
              {option.DESC_TXT}
            </option>
          ))}
        </TextField>

        {loginReducer.isError && (
          <Alert severity="error" style={{ marginBottom: 8 }}>
            {loginReducer.result}
          </Alert>
        )}

        <Button
          type="submit"
          fullWidth
          variant="contained"
          color="primary"
          disabled={isSubmitting}
          className={classes.submit}
        >
          Sign In
        </Button>
        {/* {isSubmitting && <CircularProgress style={{ marginTop: 10 }} />} */}
        <Grid container justify="flex-end">
          <a
            href="http://192.200.9.106:5000/smartapprove"
            target="_blank"
            style={{ textDecoration: "none" }}
          >
            Smart Approve
          </a>
        </Grid>

        <Grid container justify="flex-end">
          <a
            href="http://192.200.9.106:8080/UserRequest/?page=ChangePW"
            target="_blank"
            style={{ textDecoration: "none" }}
          >
            Change password
          </a>
        </Grid>

        <Grid container justify="flex-end">
          <a
            href={LinkVisitorPDF}
            target="_blank"
            style={{ textDecoration: "none" }}
          >
            คู่มือการใช้งาน (สำหรับVisitor)
          </a>
        </Grid>

        <Grid container justify="flex-end">
          <a
            href={LinkUserPDF}
            target="_blank"
            style={{ textDecoration: "none" }}
          >
            คู่มือการใช้งาน (สำหรับUser)
          </a>
        </Grid>
      </form>
    );
  };

  return (
    <Card className={classes.root}>
      <CardMedia
        className={classes.media}
        image={`${process.env.PUBLIC_URL}/images/duck.png`}
        title="Smart Purchase1111"
      />

      <CardContent>
        {/* <p>#Debug prhead {JSON.stringify(company)}</p> */}
        <Typography variant="h5">Login Welcome Visitor Systems</Typography>
        {/* HOC */}
        <Formik
          initialValues={{
            username: "",
            password: "",
            company: "",
            application: "",
            location: "",
          }}
          onSubmit={(values, { setSubmitting }) => {
            values.application = "VST";
            // alert(JSON.stringify(values));
            dispatch(loginActions.loginAuthen(values, props.history));
            // setInterval(() => {
            setSubmitting(false);
            // }, 1000);
          }}
        >
          {(props) => showForm(props)}
        </Formik>
      </CardContent>
    </Card>
  );
};

export default LoginPage;
