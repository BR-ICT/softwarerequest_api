import React, { useEffect, useState, useMemo } from "react";
import { useSelector, useDispatch } from "react-redux";
import moment from "moment";
import MaterialTable, { MTableToolbar } from "material-table";
import { makeStyles } from "@material-ui/core/styles";
import { Typography, Grid, Paper, TextField, Button } from "@material-ui/core";
import * as monitoringreceiptActions from "../../../actions/monitoringreceipt.action";
import IconButton from "@material-ui/core/IconButton";
import { Save, Delete } from "@material-ui/icons";

const useStyles = makeStyles((theme) => ({
  root: {
    width: "100%",
    marginTop: 60,
  },
  paper: {
    padding: theme.spacing(2),
    color: theme.palette.text.secondary,
  },
  margin: {
    marginTop: "0.4rem",
    marginRight: "0.4rem",
    margin: theme.spacing(0.3),
  },
  extendedIcon: {
    marginRight: theme.spacing(1),
  },
}));

export default (props) => {
  const classes = useStyles();
  const dispatch = useDispatch();
  const monitoringreceiptReducer = useSelector(
    ({ monitoringreceiptReducer }) => monitoringreceiptReducer
  );

  useEffect(() => {
    dispatch(monitoringreceiptActions.getMonitoringReceipt());
  }, []);

  // alert("xxx " + monitoringreceiptReducer.result);

  const [data, setData] = useState();

  console.log(monitoringreceiptReducer.result);

  const columns = [
    {
      title: "CONO",
      field: "CONO",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.CONO}
        </Typography>
      ),
    },
    {
      title: "DIVI",
      field: "DIVI",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.DIVI}
        </Typography>
      ),
    },
    {
      title: "RECEIPT NO.",
      field: "RCNO",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.RCNO}
        </Typography>
      ),
    },
    {
      title: "VOUCHER",
      field: "VCNO",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.VCNO}
        </Typography>
      ),
    },
    {
      title: "FIX NO.",
      field: "FIXNO",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.FIXNO}
        </Typography>
      ),
    },
  ];

  return (
    <div className={classes.root}>
      {/* Grid */}
      {/* {<p>#Debug prnumber {JSON.stringify(deptandcostReducer.result)}</p>} */}

      <MaterialTable
        icons={{}}
        editable={{
          onRowDelete: (oldData) =>
            new Promise((resolve, reject) => {
              resolve();
            }),

          onRowAdd: (newData) =>
            new Promise((resolve, reject) => {
              // setData({
              //   ...setData,
              //   rcno: newData.RCNO,
              //   voucher: newData.VCNO,
              //   fixno: newData.FIXNO,
              // });

              // alert(JSON.stringify(newData.RCNO));
              // alert(JSON.stringify(newData));

              // const updateRows = { ...data, newRow };
              // setData(updateRows);
              // resolve();
              let formData = new FormData();
              formData.append("rcno", newData.RCNO);
              formData.append("voucher", newData.VCNO);
              formData.append("fixno", newData.FIXNO);

              alert(formData.get("rcno"));
              (async function() {
                await dispatch(
                  monitoringreceiptActions.addMoniringreceipt(formData)
                );

                await dispatch(
                  monitoringreceiptActions.fetchMonitoringReceipt()
                );

                console.log(JSON.stringify(monitoringreceiptReducer.result));

                // const dataDelete = [...itemdetail];
                // const index = rowData.tableData.id;
                // dataDelete.splice(index, 1);
                // setItemDetail([...dataDelete]);
              })();

              resolve();
            }),
        }}
        id="root_pr"
        title={`Monitoring Receipt`}
        columns={columns}
        data={
          monitoringreceiptReducer.result ? monitoringreceiptReducer.result : []
        }
        options={{
          // exportButton: true,
          // toolbar: false,
          pageSize: 10,
          paging: true,
          headerStyle: {
            textAlign: "center",
            borderLeft: 1,
            borderRight: 1,
            borderBottom: 1,
            borderTop: 1,
            borderColor: "#E0E0E0",
            borderStyle: "solid",
            paddingLeft: "6px",
            paddingRight: "6px",
            paddingBottom: "12px",
            paddingTop: "12px",
            // backgroundColor: "red",
            // whiteSpace: "normal",
            // wordWrap: "break-word",
            // wordBreak: "break-all"
          },
          actionsColumnIndex: -1,
          fixedColumns: {
            // left: 2
          },
        }}
      />
    </div>
  );
};
