//package ir.maktab.investment.service.util;
//
//import ir.maktab.investment.dto.UserDto;
//import ir.maktab.investment.model.User;
//
//public class UserMapper {
//    private UserMapper() {
//
//    }
//
//    public static UserDto convertEntityToDto(User user) {
//        UserDto userDto = new UserDto();
//        userDto.setFirstname(user.getFirstname());
//        userDto.setLastname(user.getLastname());
//        userDto.setEmail(user.getEmail());
//        userDto.setPhone(user.getPhone());
//        userDto.setNationalCode(user.getNationalCode());
//        userDto.setRoles(user.getRoles());
//
//        return userDto;
//    }
//
//    public static User convertDtoToEntity(UserDto userDto,User user) {
//        user.setFirstname(userDto.getFirstname());
//        user.setLastname(userDto.getLastname());
//        user.setEmail(userDto.getEmail());
////        user.setRoles(userDto.getRoles());
//        user.setPhone(userDto.getPhone());
//        user.setNationalCode(userDto.getNationalCode());
//
//        return user;
//    }
//}
