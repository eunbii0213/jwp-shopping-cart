package woowacourse.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.member.dao.MemberDao;
import woowacourse.member.domain.Email;
import woowacourse.member.domain.Member;
import woowacourse.member.dto.MemberNameUpdateRequest;
import woowacourse.member.dto.MemberPasswordUpdateRequest;
import woowacourse.member.dto.MemberRegisterRequest;
import woowacourse.member.dto.MemberResponse;
import woowacourse.member.exception.DuplicateMemberEmailException;
import woowacourse.member.exception.NoMemberException;
import woowacourse.member.exception.WrongPasswordException;
import woowacourse.member.infrastructure.PasswordEncoder;

@Transactional
@Service
public class MemberService {

    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
    }

    public Long save(final MemberRegisterRequest memberRegisterRequest) {
        validateDuplicateEmail(memberRegisterRequest.getEmail());
        Member member = memberRegisterRequest.toEntity(passwordEncoder);
        return memberDao.save(member);
    }

    public void validateDuplicateEmail(final String email) {
        Email memberEmail = new Email(email);
        if (memberDao.isEmailExist(memberEmail.getEmail())) {
            throw new DuplicateMemberEmailException();
        }
    }

    @Transactional(readOnly = true)
    public Member login(final TokenRequest tokenRequest) {
        Member member = findByEmail(tokenRequest);

        String encode = passwordEncoder.encode(tokenRequest.getPassword());
        if (!member.authenticate(encode)) {
            throw new WrongPasswordException();
        }
        return member;
    }

    private Member findByEmail(final TokenRequest tokenRequest) {
        return memberDao.findByEmail(tokenRequest.getEmail())
                .orElseThrow(NoMemberException::new);
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberInformation(final Long id) {
        Member member = findById(id);
        return MemberResponse.from(member);
    }

    public void updateName(final Long id, final MemberNameUpdateRequest memberNameUpdateRequest) {
        Member member = findById(id);
        member.updateName(memberNameUpdateRequest.getName());
        memberDao.updateName(member);
    }

    public void updatePassword(final Long id, final MemberPasswordUpdateRequest memberPasswordUpdateRequest) {
        Member member = findById(id);
        member.updatePassword(memberPasswordUpdateRequest.getOldPassword(),
                memberPasswordUpdateRequest.getNewPassword(),
                passwordEncoder);
        memberDao.updatePassword(member);
    }

    public void deleteById(final Long id) {
        findById(id);
        memberDao.deleteById(id);
    }

    private Member findById(final Long id) {
        return memberDao.findById(id)
                .orElseThrow(NoMemberException::new);
    }
}