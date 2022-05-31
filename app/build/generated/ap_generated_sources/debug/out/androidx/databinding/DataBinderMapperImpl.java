package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new com.iotait.schoolapp.DataBinderMapperImpl());
  }
}
